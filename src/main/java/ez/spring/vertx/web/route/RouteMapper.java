package ez.spring.vertx.web.route;

import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import ez.spring.vertx.Beans;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class RouteMapper {
    private final ApplicationContext applicationContext;
    private final Router router;
    private List<RouteProps> routePropsList = Collections.emptyList();

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
                if (methods != null) {
                    for (HttpMethod method : methods) {
                        route.method(method);
                    }
                }
                // order
                Integer order = routeProps.getOrder();
                if (order != null) route.order(order);
                // handler & errorHandler
                Handler<RoutingContext> handler = getHandler(routeProps.getHandler());
                Handler<RoutingContext> errorHandler = getHandler(routeProps.getErrorHandler());
                if (handler != null) {
                    route.handler(handler);
                    log.info("mapping {}{} to handler: {}",
                            path == null ? "/*" : path,
                            toJson(methods),
                            handler.getClass().getCanonicalName());
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
}
