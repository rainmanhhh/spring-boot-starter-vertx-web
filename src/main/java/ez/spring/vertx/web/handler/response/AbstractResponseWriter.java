package ez.spring.vertx.web.handler.response;

import org.springframework.lang.Nullable;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;

public abstract class AbstractResponseWriter<Response> implements ResponseWriter<Response> {
  public abstract String getContentType();

  @Override
  public void writeResponse(RoutingContext context, @Nullable Response response) {
    context.response().putHeader(
      HttpHeaders.CONTENT_TYPE, getContentType()
    ).end(
      encodeBody(response)
    );
  }

  public abstract Buffer encodeBody(@Nullable Response response);
}
