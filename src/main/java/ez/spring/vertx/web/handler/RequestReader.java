package ez.spring.vertx.web.handler;

import io.vertx.ext.web.RoutingContext;

public interface RequestReader<Request> {
    Request readRequest(RoutingContext context) throws Throwable;
}
