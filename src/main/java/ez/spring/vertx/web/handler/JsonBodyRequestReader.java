package ez.spring.vertx.web.handler;

import org.springframework.lang.Nullable;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

@SuppressWarnings("WeakerAccess")
public class JsonBodyRequestReader<Request> implements RequestReader<Request> {
    private final Class<Request> requestClass;

    public JsonBodyRequestReader(Class<Request> requestClass) {
        this.requestClass = requestClass;
    }

    @Nullable
    @Override
    public Request readRequest(RoutingContext context) {
        return Json.mapper.convertValue(context.getBodyAsJson(), requestClass);
    }
}