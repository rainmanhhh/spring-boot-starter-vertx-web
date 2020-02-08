package ez.spring.vertx.web.handler.response;

import org.springframework.lang.Nullable;

import java.nio.charset.Charset;

import io.vertx.ext.web.RoutingContext;

public interface ResponseWriter<Response> {

  /**
   * @param <Response> response object type
   * @return utf-8 json body writer
   */
  @SuppressWarnings("unchecked")
  static <Response> JsonResponseWriter<Response> json() {
    return (JsonResponseWriter<Response>) JsonResponseWriter.INSTANCE;
  }

  static TextResponseWriter text(Charset charset) {
    return new TextResponseWriter(charset);
  }

  static TextResponseWriter text() {
    return TextResponseWriter.INSTANCE;
  }

  static NoContentResponseWriter none() {
    return NoContentResponseWriter.INSTANCE;
  }

  void writeResponse(RoutingContext context, @Nullable Response response);
}