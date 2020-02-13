package ez.spring.vertx.web.handler;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface RoutingHandler extends Handler<RoutingContext> {
  @Override
  default void handle(RoutingContext event) {
    exec(event).setHandler(r -> {
      if (r.succeeded()) event.next();
      else event.fail(r.cause());
    });
  }

  Future<?> exec(RoutingContext event);
}
