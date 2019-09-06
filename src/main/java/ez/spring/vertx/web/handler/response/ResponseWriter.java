package ez.spring.vertx.web.handler.response;

import io.vertx.ext.web.RoutingContext;

public interface ResponseWriter<Response> {
    void writeResponse(RoutingContext context, Response response);

    /**
     *
     * @return utf-8 json body writer
     */
    static <Response> JsonResponseWriter<Response> json() {
        return new JsonResponseWriter<>();
    }

    static TextResponseWriter text() {
        return new TextResponseWriter();
    }
}