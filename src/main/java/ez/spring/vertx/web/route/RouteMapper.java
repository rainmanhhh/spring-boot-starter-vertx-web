package ez.spring.vertx.web.route;

import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.List;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.Data;

@Data
public class RouteMapper {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApplicationContext applicationContext;
    private final Router router;
    private List<RouteProps> routePropsList = Collections.emptyList();

    @SuppressWarnings("unused")
    public Router map() {
        for (int i = 0; i < routePropsList.size(); i++) {
            RouteProps routeProps = routePropsList.get(i);
            try {
                String path = routeProps.getPath();
                Route route = path == null ? router.route() : router.route(path);
                List<HttpMethod> methods = routeProps.getMethods();
                if (!methods.isEmpty()) {
                    for (HttpMethod method : methods) {
                        route.method(method);
                    }
                }
                Class<? extends Handler<RoutingContext>> handlerType = getHandlerType(routeProps.getHandler());
                Class<? extends Handler<RoutingContext>> errorHandlerType = getHandlerType(routeProps.getErrorHandler());
                if (handlerType != null) {
                    Handler<RoutingContext> handler = applicationContext.getBean(handlerType);
                    route.handler(handler);
                    logger.info("mapping {}{} to handler: {}", path == null ? "/*" : path, Json.encode(methods),
                            handler == null ? null : handler.getClass().getCanonicalName());
                }
                if (errorHandlerType != null) {
                    Handler<RoutingContext> errorHandler = applicationContext.getBean(errorHandlerType);
                    route.failureHandler(errorHandler);
                    logger.info("mapping {}{} to errorHandler: {}", path == null ? "/*" : path, Json.encode(methods),
                            errorHandler == null ? null : errorHandler.getClass().getCanonicalName());
                }
                if (handlerType == null && errorHandlerType == null) {
                    logger.error("routePropsList[{}] has no handler/errorHandler: {}", i, Json.encode(routeProps));
                }
            } catch (Throwable err) {
                logger.error("mapping routes[{}]: {} failed", i, Json.encode(routeProps), err);
                throw new RuntimeException(err);
            }
        }
        return router;
    }

    private Class<? extends Handler<RoutingContext>> getHandlerType(String typeName) throws ClassNotFoundException {
        if (typeName == null) return null;
        ClassLoader beanClassLoader = applicationContext.getClassLoader();
        if (beanClassLoader == null)
            throw new NullPointerException("applicationContext.classLoader is null");
        Class<?> type = beanClassLoader.loadClass(typeName);
        @SuppressWarnings("unchecked")
        Class<? extends Handler<RoutingContext>> handlerType = (Class<? extends Handler<RoutingContext>>) type;
        return handlerType;
    }
}
