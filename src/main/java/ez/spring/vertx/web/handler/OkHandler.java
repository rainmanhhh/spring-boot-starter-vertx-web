package ez.spring.vertx.web.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class OkHandler implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext event) {
        event.response().end();
    }
}
