package ez.spring.vertx.web.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import ez.spring.vertx.bean.Beans;
import ez.spring.vertx.web.handler.OptionsHandler;
import ez.spring.vertx.web.handler.WebHandler;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class EzRouterUtil {
  private static final Logger log = LoggerFactory.getLogger(EzRouterUtil.class);

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

  public static Router setup(Router router, RouteProps props) {
    // path
    String path = props.getPath();
    Route route = path == null ? router.route() : router.route(path);
    // methods
    Set<HttpMethod> methods = props.getMethods();
    setMethods(route, methods);
    // order
    Integer order = props.getOrder();
    if (order != null) route.order(order);
    // subRouter
    Router subRouter = getBean(props.getSubRouter());
    if (subRouter != null) {
      router.mountSubRouter(path, subRouter);
    } else {
      // handler & errorHandler
      Handler<RoutingContext> handler = getBean(props.getHandler());
      Handler<RoutingContext> errorHandler = getBean(props.getErrorHandler());
      if (handler != null) {
        // optionsHandler
        boolean withOptionsHandler = props.isWithOptionsHandler();
        if (handler instanceof WebHandler)
          withOptionsHandler = ((WebHandler<?, ?>) handler).isWithOptionsHandler();
        if (withOptionsHandler) {
          Route optionsRoute = path == null ? router.route() : router.route(path);
          optionsRoute.method(HttpMethod.OPTIONS);
          optionsRoute.handler(new OptionsHandler(methods));
        }
        route.handler(handler);
      }
      if (errorHandler != null) route.failureHandler(errorHandler);
    }
    return router;
  }

  public static Router setup(Router router, List<RouteProps> routePropsList) {
    for (int i = 0; i < routePropsList.size(); i++) {
      RouteProps routeProps = routePropsList.get(i);
      try {
        setup(router, routeProps);
      } catch (Throwable err) {
        log.error("mapping routes[{}] failed, props: {}", i, Json.encode(routeProps), err);
        throw new RuntimeException(err);
      }
    }
    return router;
  }

  public static Router setup(Vertx vertx, List<RouteProps> routePropsList) {
    return setup(Router.router(vertx), routePropsList);
  }

  private static <T> T getBean(String descriptor) {
    if (descriptor == null) return null;
    else return Beans.<T>withDescriptor(descriptor).getBean();
  }

  private static void setMethods(Route route, Set<HttpMethod> methods) {
    if (methods != null) {
      for (HttpMethod method : methods) {
        route.method(method);
      }
    }
  }
}