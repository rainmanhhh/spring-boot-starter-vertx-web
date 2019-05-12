package ez.spring.vertx.web.verticle;

import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.List;

import ez.spring.vertx.web.route.RouteMapper;
import ez.spring.vertx.web.route.RouteProps;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpServerVerticle extends AbstractVerticle {
    private final HttpServer httpServer;
    private final RouteMapper routeMapper;
    @Getter
    @Setter
    private List<RouteProps> routes = Collections.emptyList();

    public HttpServerVerticle(
            ApplicationContext applicationContext,
            HttpServer httpServer,
            Router router
    ) {
        this.httpServer = httpServer;
        this.routeMapper = new RouteMapper(applicationContext, router);
    }

    @Override
    public void start(Future<Void> startFuture) {
        routeMapper.setRoutePropsList(routes);
        httpServer.requestHandler(
                routeMapper.map()
        ).listen(event -> {
            if (event.succeeded()) {
                log.info("httpServer listening at port {}", event.result().actualPort());
                startFuture.complete();
            } else startFuture.fail(event.cause());
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        httpServer.close(event -> {
            if (event.succeeded()) {
                log.info("httpServer at port {} stopped", httpServer.actualPort());
                stopFuture.complete();
            } else stopFuture.fail(event.cause());
        });
    }
}
