package ez.spring.vertx.web.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.HttpStatusException;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class WebHandler<Request, Response> implements Handler<RoutingContext> {
    public enum KnownError {
        DECODE_REQUEST_FAILED
    }

    private final Class<Request> requestClass;
    private RequestReader<Request> requestReader;
    private ResponseWriter<Response> responseWriter;

    protected WebHandler(Class<Request> requestClass) {
        this.requestClass = requestClass;
        requestReader = new JsonBodyRequestReader<>(requestClass);
        responseWriter = new JsonResponseWriter<>();
    }

    @Override
    public final void handle(RoutingContext event) {
        final Request request;
        try {
            request = requestReader.readRequest(event);
        } catch (Throwable err) {
            fail(event, new HttpStatusException(
                    HttpResponseStatus.BAD_REQUEST.code(),
                    KnownError.DECODE_REQUEST_FAILED.name(), err
            ));
            return;
        }
        try {
            Future<Response> responseFuture = exec(request);
            responseFuture.setHandler(asyncResult -> {
                if (asyncResult.succeeded())
                    responseWriter.writeResponse(event, asyncResult.result());
                else fail(event, asyncResult.cause());
            });
        } catch (Throwable err) {
            fail(event, err);
        }
    }

    private void fail(RoutingContext context, Throwable err) {
        if (err instanceof HttpStatusException) {
            HttpStatusException statusException = (HttpStatusException) err;
            context.response().setStatusMessage(statusException.getPayload());
            context.fail(statusException.getStatusCode(), err);
        } else context.fail(err);
    }

    public WebHandler<Request, Response> setRequestReader(RequestReader<Request> requestReader) {
        this.requestReader = requestReader;
        return this;
    }

    public WebHandler<Request, Response> setResponseWriter(ResponseWriter<Response> responseWriter) {
        this.responseWriter = responseWriter;
        return this;
    }

    public WebHandler<Request, Response> useQs() {
        return setRequestReader(new QsRequestReader<>(requestClass));
    }

    public abstract Future<Response> exec(Request request) throws Throwable;
}
