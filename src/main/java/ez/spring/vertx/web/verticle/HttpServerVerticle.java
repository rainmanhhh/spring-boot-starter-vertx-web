package ez.spring.vertx.web.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import ez.spring.vertx.web.route.EzRouterUtil;
import ez.spring.vertx.web.route.RouteProps;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;

@SuppressWarnings("WeakerAccess")
public class HttpServerVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(HttpServerVerticle.class);
  private final HttpServerOptions options;
  private final List<RouteProps> routes;

  public HttpServerVerticle(HttpServerOptions options, List<RouteProps> routes) {
    this.options = options;
    this.routes = routes;
  }

  public List<RouteProps> getRoutes() {
    return routes;
  }

  @Override
  public void start(Promise<Void> startPromise) {
    HttpServer httpServer = vertx.createHttpServer(options);
    Router router = EzRouterUtil.setup(vertx, getRoutes());
    httpServer.requestHandler(router).listen(it -> {
      if (it.succeeded()) {
        log.info("using httpServer at {}:{}", options.getHost(), it.result().actualPort());
        startPromise.complete();
      } else {
        log.error("httpServer not available! options: {}", Json.encodePrettily(options));
        startPromise.fail(it.cause());
      }
    });
  }
}
