package ez.spring.vertx.web.handler.request;

import io.vertx.ext.web.RoutingContext;

public interface RequestReader<Request> {
    Request readRequest(RoutingContext context) throws Throwable;
}
