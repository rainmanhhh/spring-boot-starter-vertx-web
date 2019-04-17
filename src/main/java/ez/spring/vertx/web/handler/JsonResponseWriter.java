package ez.spring.vertx.web.handler;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class JsonResponseWriter<Response> implements ResponseWriter<Response> {
    @Override
    public void writeResponse(RoutingContext context, Response response) {
        context.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8")
                .end(Json.encodeToBuffer(response));
    }
}