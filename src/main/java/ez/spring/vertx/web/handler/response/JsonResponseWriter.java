package ez.spring.vertx.web.handler.response;

import org.springframework.lang.Nullable;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;

/**
 * utf-8 json body writer
 */
public class JsonResponseWriter<Response> extends AbstractResponseWriter<Response> {

  public static final JsonResponseWriter<Object> INSTANCE = new JsonResponseWriter<>();

  /**
   * hardcoded to utf-8 because Json.encodeToBuffer hardcoded to utf-8
   *
   * @return http header: contentType
   */
  @Override
  public final String getContentType() {
    return "application/json;charset=utf-8";
  }

  @Override
  public final Buffer encodeBody(@Nullable Response response) {
    return Json.encodeToBuffer(response);
  }
}