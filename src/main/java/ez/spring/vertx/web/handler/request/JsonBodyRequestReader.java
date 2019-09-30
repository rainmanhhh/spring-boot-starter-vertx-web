package ez.spring.vertx.web.handler.request;

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.springframework.lang.Nullable;

/**
 * request reader for json body. eg: decode `{a:1, b:"cde"}` to {a:1, b:"cde"}
 *
 * @param <Request> request object type
 */
@SuppressWarnings("WeakerAccess")
public class JsonBodyRequestReader<Request> implements RequestReader<Request> {
    private final Class<Request> requestClass;

    public JsonBodyRequestReader(Class<Request> requestClass) {
        this.requestClass = requestClass;
    }

    @Nullable
    @Override
    public Future<Request> readRequest(RoutingContext context) {
        return Future.succeededFuture(Json.mapper.convertValue(context.getBodyAsJson(), requestClass));
    }
}