package ez.spring.vertx.web.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.List;

import ez.spring.vertx.web.route.RouteMapper;
import ez.spring.vertx.web.route.RouteProps;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

public class HttpServerVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(HttpServerVerticle.class);
    private final HttpServerOptions httpServerOptions;
    private final RouteMapper routeMapper;
    private List<RouteProps> routes = Collections.emptyList();
    private HttpServer httpServer = null;

    public HttpServerVerticle(
            ApplicationContext applicationContext,
            HttpServerOptions httpServerOptions,
            Router router
    ) {
        this.httpServerOptions = httpServerOptions;
        this.routeMapper = new RouteMapper(applicationContext, router);
    }

    public List<RouteProps> getRoutes() {
        return routes;
    }

    public HttpServerVerticle setRoutes(List<RouteProps> routes) {
        this.routes = routes;
        return this;
    }

    @Override
    public void start(Future<Void> startFuture) {
        routeMapper.setRoutePropsList(routes);
        httpServer = vertx.createHttpServer(httpServerOptions);
        httpServer.requestHandler(
                routeMapper.map()
        ).listen(event -> {
            if (event.succeeded()) {
                log.info("httpServer listening at {}:{}",
                        httpServerOptions.getHost(), event.result().actualPort());
                startFuture.complete();
            } else {
                startFuture.fail(new RuntimeException(
                        "httpServer listen failed! options: "
                                + httpServerOptions.toJson().encodePrettily(),
                        event.cause()
                ));
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        if (httpServer != null) {
            httpServer.close(event -> {
                if (event.succeeded()) {
                    log.info("httpServer at {}:{} stopped",
                            httpServerOptions.getHost(), httpServer.actualPort());
                    stopFuture.complete();
                } else stopFuture.fail(event.cause());
            });
        }
    }
}
