package ez.spring.vertx.web.handler.request;

import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

public interface RequestReader<Request> {
    /**
     * @param requestClass decoded request class
     * @param <Request>    decoded request class
     * @return requestReader
     * @see JsonBodyRequestReader
     */
    static <Request> JsonBodyRequestReader<Request> jsonBody(Class<Request> requestClass) {
        return new JsonBodyRequestReader<>(requestClass);
    }

    /**
     * @param requestClass decoded request class
     * @param <Request>    decoded request class
     * @return requestReader
     * @see QsRequestReader
     */
    static <Request> QsRequestReader<Request> qs(Class<Request> requestClass) {
        return new QsRequestReader<>(requestClass);
    }

    Future<Request> readRequest(RoutingContext context) throws Throwable;
}
