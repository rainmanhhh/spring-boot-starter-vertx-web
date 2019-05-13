package ez.spring.vertx.web.handler.response;

import io.vertx.ext.web.RoutingContext;

public interface ResponseWriter<Response> {
    void writeResponse(RoutingContext context, Response response);
}