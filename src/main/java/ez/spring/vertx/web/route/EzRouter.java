package ez.spring.vertx.web.route;

import ez.spring.vertx.Beans;
import ez.spring.vertx.util.EzUtil;
import ez.spring.vertx.web.handler.OptionsHandler;
import ez.spring.vertx.web.handler.WebHandler;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class EzRouter implements Handler<HttpServerRequest> {
    private static final Logger log = LoggerFactory.getLogger(EzRouter.class);
    private final Router router;
    private final List<RouteProps> routePropsList;

    private EzRouter(Router router, List<RouteProps> routePropsList) {
        this.router = router;
        this.routePropsList = routePropsList;
    }

    /**
     * should call this method in vertx thread(verticle.start)
     *
     * @param routePropsList route props list
     * @return {@link EzRouter}
     */
    public static EzRouter router(List<RouteProps> routePropsList) {
        Vertx vertx = EzUtil.vertx();
        EzRouter ezRouter = new EzRouter(Router.router(vertx), routePropsList);
        ezRouter.map();
        return ezRouter;
    }

    public static void printRoutes(Collection<RouteProps> routes) {
        int i = 0;
        for (RouteProps r : routes) {
            String path = r.getPath();
            String handler = r.getHandler();
            String errorHandler = r.getErrorHandler();
            String methods = r.getMethods() == null ? "" : Json.encode(r.getMethods());
            log.info("route: {}{}, handler: {}{}, errorHandler:{}",
                    path == null ? "/*" : path,
                    methods,
                    handler,
                    r.isWithOptionsHandler() ? " (support OPTIONS)" : "",
                    errorHandler
            );
            if (handler == null && errorHandler == null) {
                log.error("mainRouteProps[{}] has no handler/errorHandler: {}", i, Json.encode(r));
            }
            i++;
        }
    }

    private void map() {
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
                }
                if (errorHandler != null) route.failureHandler(errorHandler);
            } catch (Throwable err) {
                log.error("mapping routes[{}]: {} failed", i, Json.encode(routeProps), err);
                throw new RuntimeException(err);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Handler<RoutingContext> getHandler(String descriptor) {
        if (descriptor == null) return null;
        else return ((Handler<RoutingContext>) Beans.withDescriptor(descriptor).allowImplicit().get());
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

    @Override
    public void handle(HttpServerRequest event) {
        router.handle(event);
    }
}