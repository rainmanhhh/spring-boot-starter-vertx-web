package ez.spring.vertx.web.handler;

import org.springframework.lang.Nullable;

import ez.spring.vertx.web.handler.request.RequestReader;
import ez.spring.vertx.web.handler.response.ResponseWriter;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.HttpStatusException;

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
    final Future<Request> request;
    try {
      request = getRequestReader().readRequest(event);
    } catch (Throwable err) {
      doFail(event, KnownError.DECODE_REQUEST_FAILED.withCause(err));
      return;
    }
    try {
      request.compose(this::exec).onComplete(it -> {
        if (it.succeeded()) getResponseWriter().writeResponse(event, it.result());
        else doFail(event, it.cause());
      });
    } catch (Throwable err) {
      doFail(event, err);
    }
  }

  public abstract Future<Response> exec(Request request);

  public abstract RequestReader<Request> getRequestReader();

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
