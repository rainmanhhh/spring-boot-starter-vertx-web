package ez.spring.vertx.web.handler.response;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import org.springframework.lang.Nullable;

/**
 * utf-8 json body writer
 */
public class JsonResponseWriter<Response> extends AbstractResponseWriter<Response> {

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
    public Buffer encodeBody(@Nullable Response response) {
        return Json.encodeToBuffer(response);
    }
}