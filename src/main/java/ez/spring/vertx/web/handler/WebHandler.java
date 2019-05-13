package ez.spring.vertx.web.handler;

import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import ez.spring.vertx.util.ParameterizedTypeHelper;
import ez.spring.vertx.web.handler.request.JsonBodyRequestReader;
import ez.spring.vertx.web.handler.request.QsRequestReader;
import ez.spring.vertx.web.handler.request.RequestReader;
import ez.spring.vertx.web.handler.response.JsonResponseWriter;
import ez.spring.vertx.web.handler.response.ResponseWriter;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.HttpStatusException;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class WebHandler<Request, Response> implements Handler<RoutingContext> {
    private final Class<Request> requestClass;
    private RequestReader<Request> requestReader;
    private ResponseWriter<Response> responseWriter;
    protected WebHandler() {
        this.requestClass = ParameterizedTypeHelper.of(this.getClass(), WebHandler.class).get(0);
        requestReader = new JsonBodyRequestReader<>(requestClass);
        responseWriter = new JsonResponseWriter<>();
    }

    @Override
    public final void handle(RoutingContext event) {
        final Request request;
        try {
            request = requestReader.readRequest(event);
        } catch (Throwable err) {
            fail(event, KnownError.DECODE_REQUEST_FAILED.error(err));
            return;
        }
        try {
            CompletionStage<Response> responseFuture = exec(request);
            responseFuture.thenAccept(response ->
                    responseWriter.writeResponse(event, response)
            ).exceptionally(throwable -> {
                if (throwable instanceof CompletionException)
                    fail(event, throwable.getCause());
                else
                    fail(event, throwable);
                return null;
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

    public abstract CompletionStage<Response> exec(Request request) throws Throwable;

    public enum KnownError {
        DECODE_REQUEST_FAILED(HttpResponseStatus.BAD_REQUEST);
        private HttpResponseStatus status;
        private HttpStatusException ex;

        KnownError(HttpResponseStatus status) {
            this.status = status;
            ex = new HttpStatusException(status.code(), name());
        }

        public HttpStatusException error(Throwable cause) {
            return cause == null ? ex : new HttpStatusException(status.code(), name());
        }

        public HttpStatusException error() {
            return error(null);
        }
    }
}
