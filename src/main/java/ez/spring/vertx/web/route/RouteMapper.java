package ez.spring.vertx.web.route;

import ez.spring.vertx.Beans;
import ez.spring.vertx.web.handler.OptionsHandler;
import ez.spring.vertx.web.handler.WebHandler;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RouteMapper {
    private static final Logger log = LoggerFactory.getLogger(RouteMapper.class);
    private final Router router;
    private List<RouteProps> routePropsList = Collections.emptyList();

    public RouteMapper(Router router) {
        this.router = router;
    }

    private String toJson(Collection<?> collection) {
        return collection == null ? "" : Json.encode(collection);
    }

    @SuppressWarnings("unused")
    public Router map() {
        for (int i = 0; i < routePropsList.size(); i++) {
            RouteProps routeProps = routePropsList.get(i);
            try {
                // path
                String path = routeProps.getPath();
                Route route = path == null ? router.route() : router.route(path);
                // methods
                Set<HttpMethod> methods = routeProps.getMethods();
                setMethods(route, methods);
                // order
                Integer order = routeProps.getOrder();
                if (order != null) route.order(order);
                // handler & errorHandler
                Handler<RoutingContext> handler = getHandler(routeProps.getHandler());
                Handler<RoutingContext> errorHandler = getHandler(routeProps.getErrorHandler());
                if (handler != null) {
                    // optionsHandler
                    boolean withOptionsHandler = routeProps.isWithOptionsHandler();
                    if (handler instanceof WebHandler)
                        withOptionsHandler = ((WebHandler) handler).isWithOptionsHandler();
                    if (withOptionsHandler) {
                        Route optionsRoute = path == null ? router.route() : router.route(path);
                        optionsRoute.method(HttpMethod.OPTIONS);
                        optionsRoute.handler(new OptionsHandler(methods));
                    }
                    route.handler(handler);
                    log.info("mapping {}{} to handler: {}{}",
                            path == null ? "/*" : path,
                            toJson(methods),
                            handler.getClass().getCanonicalName(),
                            withOptionsHandler ? " (support OPTIONS)" : ""
                    );
                }
                if (errorHandler != null) {
                    route.failureHandler(errorHandler);
                    log.info("mapping {}{} to errorHandler: {}",
                            path == null ? "/*" : path,
                            toJson(methods),
                            errorHandler.getClass().getCanonicalName());
                }
                if (handler == null && errorHandler == null) {
                    log.error("routePropsList[{}] has no handler/errorHandler: {}", i, Json.encode(routeProps));
                }
            } catch (Throwable err) {
                log.error("mapping routes[{}]: {} failed", i, Json.encode(routeProps), err);
                throw new RuntimeException(err);
            }
        }
        return router;
    }

    private Handler<RoutingContext> getHandler(String descriptor) {
        if (descriptor == null) return null;
        else return Beans.get(descriptor);
    }

    private void setMethods(Route route, Set<HttpMethod> methods) {
        if (methods != null) {
            for (HttpMethod method : methods) {
                route.method(method);
            }
        }
    }

    public Router getRouter() {
        return router;
    }

    public List<RouteProps> getRoutePropsList() {
        return routePropsList;
    }

    public RouteMapper setRoutePropsList(List<RouteProps> routePropsList) {
        this.routePropsList = routePropsList;
        return this;
    }
}