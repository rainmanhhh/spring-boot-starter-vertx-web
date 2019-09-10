package ez.spring.vertx.web.verticle;

import ez.spring.vertx.web.route.RouteMapper;
import ez.spring.vertx.web.route.RouteProps;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.List;

public class HttpServerVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(HttpServerVerticle.class);
    private final HttpServerOptions httpServerOptions;
    private RouteMapper routeMapper;
    private List<RouteProps> routes = Collections.emptyList();
    private HttpServer httpServer = null;

    public HttpServerVerticle(HttpServerOptions httpServerOptions) {
        this.httpServerOptions = httpServerOptions;
    }

    @Nullable
    public RouteMapper getRouteMapper() {
        return routeMapper;
    }

    public List<RouteProps> getRoutes() {
        return routes;
    }

    public HttpServerVerticle setRoutes(List<RouteProps> routes) {
        this.routes = routes;
        return this;
    }

    @Nullable
    public HttpServer getHttpServer() {
        return httpServer;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        httpServer = vertx.createHttpServer(httpServerOptions);
        routeMapper = new RouteMapper(Router.router(vertx));
        routeMapper.setRoutePropsList(routes);
        httpServer.requestHandler(
                routeMapper.map()
        ).listen(event -> {
            if (event.succeeded()) {
                log.info("httpServer listening at {}:{}", httpServerOptions.getHost(), event.result().actualPort());
                startPromise.complete();
            } else {
                log.error("httpServer start failed, options: {}", Json.encodePrettily(httpServerOptions));
                startPromise.fail(event.cause());
            }
        });
    }

    @Override
    public void stop(Promise<Void> stopPromise) {
        if (httpServer != null) {
            httpServer.close(event -> {
                if (event.succeeded()) {
                    log.info("httpServer at {} stopped", httpServer.actualPort());
                    stopPromise.complete();
                } else stopPromise.fail(event.cause());
            });
        }
    }
}
