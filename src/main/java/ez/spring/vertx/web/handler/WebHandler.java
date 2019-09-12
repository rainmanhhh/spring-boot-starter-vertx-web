package ez.spring.vertx.web.handler;

import ez.spring.vertx.web.handler.request.RequestReader;
import ez.spring.vertx.web.handler.response.ResponseWriter;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import org.springframework.lang.Nullable;

@SuppressWarnings({"unused"})
public abstract class WebHandler<Request, Response> implements Handler<RoutingContext> {
    private boolean withOptionsHandler = true;

    private static void fail(RoutingContext context, Throwable err) {
        if (err instanceof HttpStatusException) {
            HttpStatusException statusException = (HttpStatusException) err;
            context.response().setStatusMessage(statusException.getPayload());
            context.fail(statusException.getStatusCode(), err);
        } else context.fail(err);
    }

    @Override
    public final void handle(RoutingContext routingContext) {
        final RequestReader<Request> requestReader = getRequestReader();
        final ResponseWriter<Response> responseWriter = getResponseWriter();
        final Request request;
        try {
            request = requestReader == null ? null : requestReader.readRequest(routingContext);
        } catch (Throwable err) {
            KnownError.DECODE_REQUEST_FAILED.doFail(routingContext, err);
            fail(routingContext, KnownError.DECODE_REQUEST_FAILED.wrap(err));
            return;
        }
        try {
            exec(request).setHandler(r -> {
                if (r.succeeded()) responseWriter.writeResponse(routingContext, r.result());
                else fail(routingContext, r.cause());
            });
        } catch (Throwable err) {
            fail(routingContext, err);
        }
    }

    public abstract Future<Response> exec(@Nullable Request request) throws Throwable;

    @Nullable
    abstract public RequestReader<Request> getRequestReader();

    abstract public ResponseWriter<Response> getResponseWriter();

    public boolean isWithOptionsHandler() {
        return withOptionsHandler;
    }

    public WebHandler<Request, Response> setWithOptionsHandler(boolean withOptionsHandler) {
        this.withOptionsHandler = withOptionsHandler;
        return this;
    }

    public enum KnownError {
        DECODE_REQUEST_FAILED(HttpResponseStatus.BAD_REQUEST);
        private HttpResponseStatus status;
        private HttpStatusException ex;

        KnownError(HttpResponseStatus status) {
            this.status = status;
            ex = new HttpStatusException(status.code(), name());
        }

        public HttpStatusException wrap(@Nullable Throwable cause) {
            return cause == null ? ex : new HttpStatusException(status.code(), name());
        }

        public HttpStatusException wrap() {
            return wrap(null);
        }

        public void doFail(RoutingContext routingContext, @Nullable Throwable cause) {
            WebHandler.fail(routingContext, wrap(cause));
        }
    }
}
