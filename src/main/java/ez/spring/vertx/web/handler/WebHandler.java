package ez.spring.vertx.web.handler;

import ez.spring.vertx.web.handler.request.RequestReader;
import ez.spring.vertx.web.handler.response.ResponseWriter;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import org.springframework.lang.Nullable;

public abstract class WebHandler<Request, Response> implements Handler<RoutingContext> {
    private boolean isWithOptionsHandler = true;

    public boolean isWithOptionsHandler() {
        return isWithOptionsHandler;
    }

    public WebHandler<Request, Response> setWithOptionsHandler(boolean withOptionsHandler) {
        isWithOptionsHandler = withOptionsHandler;
        return this;
    }

    @Override
    public void handle(RoutingContext event) {
        RequestReader<Request> requestReader = getRequestReader();
        ResponseWriter<Response> responseWriter = getResponseWriter();
        final Future<Request> request;
        try {
            request = requestReader == null ? Future.succeededFuture(null) : requestReader.readRequest(event);
        } catch (Throwable err) {
            doFail(event, KnownError.DECODE_REQUEST_FAILED.withCause(err));
            return;
        }
        try {
            request.compose(this::exec).setHandler(it -> {
                if (it.succeeded()) {
                    if (responseWriter == null)
                        event.response().setStatusCode(
                                HttpResponseStatus.NO_CONTENT.code()
                        ).end();
                    else responseWriter.writeResponse(event, it.result());
                } else doFail(event, it.cause());
            });
        } catch (Throwable err) {
            doFail(event, err);
        }
    }

    public abstract Future<Response> exec(@Nullable Request request);

    @Nullable
    public abstract RequestReader<Request> getRequestReader();

    @Nullable
    public abstract ResponseWriter<Response> getResponseWriter();

    private void doFail(RoutingContext context, Throwable err) {
        if (err instanceof HttpStatusException) {
            HttpStatusException he = (HttpStatusException) err;
            context.response().setStatusMessage((he).getPayload());
            context.fail((he).getStatusCode(), err);
        } else context.fail(err);
    }

    enum KnownError {
        DECODE_REQUEST_FAILED(HttpResponseStatus.BAD_REQUEST);

        private final HttpResponseStatus status;
        private final HttpStatusException ex;

        KnownError(HttpResponseStatus status) {
            this.status = status;
            ex = new HttpStatusException(status.code(), name());
        }

        HttpStatusException withCause(@Nullable Throwable cause) {
            return cause == null ? ex : new HttpStatusException(status.code(), name(), cause);
        }

        HttpStatusException withCause() {
            return withCause(null);
        }
    }
}
