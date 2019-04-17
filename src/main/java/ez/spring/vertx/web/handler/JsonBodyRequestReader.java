package ez.spring.vertx.web.handler;

import io.vertx.ext.web.RoutingContext;

@SuppressWarnings("WeakerAccess")
public class JsonBodyRequestReader<Request> implements RequestReader<Request> {
    private final Class<Request> requestClass;

    public JsonBodyRequestReader(Class<Request> requestClass) {
        this.requestClass = requestClass;
    }

    @Override
    public Request readRequest(RoutingContext context) {
        return context.getBodyAsJson().mapTo(requestClass);
    }
}