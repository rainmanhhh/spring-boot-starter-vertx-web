package ez.spring.vertx.web.handler;

import io.vertx.ext.web.RoutingContext;

public interface ResponseWriter<Response> {
    void writeResponse(RoutingContext context, Response response);
}