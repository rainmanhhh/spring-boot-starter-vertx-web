package ez.spring.vertx.web.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import ez.spring.vertx.web.route.EzRouter;
import ez.spring.vertx.web.route.RouteProps;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;

@SuppressWarnings("WeakerAccess")
public class HttpServerVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(HttpServerVerticle.class);
  private final HttpServerOptions options;
  private List<RouteProps> routes = Collections.emptyList();
  private HttpServer httpServer = null;
  private EzRouter router = null;

  public HttpServerVerticle(HttpServerOptions options) {
    this.options = options;
  }

  public HttpServerVerticle setRoutes(List<RouteProps> routes) {
    this.routes = routes;
    return this;
  }

  @Override
  public void start(Promise<Void> startPromise) {
    httpServer = vertx.createHttpServer(options);
    router = EzRouter.router(routes);
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
