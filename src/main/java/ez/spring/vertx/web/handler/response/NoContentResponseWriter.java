package ez.spring.vertx.web.handler.response;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.ext.web.RoutingContext;

public class NoContentResponseWriter implements ResponseWriter<Object> {
  @Override
  public final void writeResponse(RoutingContext context, Object o) {
    context.response().setStatusCode(
      HttpResponseStatus.NO_CONTENT.code()
    ).end();
  }

  public static final NoContentResponseWriter INSTANCE = new NoContentResponseWriter();
}
