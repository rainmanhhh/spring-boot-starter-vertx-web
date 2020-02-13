package ez.spring.vertx.web.handler.request;

import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

public class NoneRequestReader<Request> implements RequestReader<Request> {
  public static final NoneRequestReader<Object> INSTANCE = new NoneRequestReader<>();
  private static final Future<Object> FUTURE = Future.succeededFuture(new Object());

  @SuppressWarnings("unchecked")
  @Override
  public final Future<Request> readRequest(RoutingContext context) {
    return (Future<Request>) FUTURE;
  }
}
