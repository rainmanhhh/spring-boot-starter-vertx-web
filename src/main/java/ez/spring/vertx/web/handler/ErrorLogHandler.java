package ez.spring.vertx.web.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("WeakerAccess")
public class ErrorLogHandler implements Handler<RoutingContext> {
    private static final Logger log = LoggerFactory.getLogger(ErrorLogHandler.class);
    private int errorCode = HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
    private int warnCode = HttpResponseStatus.BAD_REQUEST.code();
    private boolean showWarnStack = false;

    @Override
    public void handle(RoutingContext event) {
        event.addBodyEndHandler(v -> log(event));
        event.next();
    }

    protected void log(RoutingContext context) {
        Throwable failure = context.failure();
        if (failure != null) {
            String errClass = failure.getClass().getCanonicalName();
            if (failure instanceof HttpStatusException) {
                HttpStatusException se = (HttpStatusException) failure;
                int statusCode = se.getStatusCode();
                String payload = se.getPayload();
                Throwable cause = se.getCause();
                if (failure.getClass() == HttpStatusException.class) errClass = "";
                if (statusCode >= getErrorCode()) {
                    log.error("http-{} {}: {}", statusCode, errClass, payload, cause);
                } else if (statusCode >= getWarnCode()) {
                    if (isShowWarnStack())
                        log.warn("http-{} {}: {}", statusCode, errClass, payload, cause);
                    else log.warn("http-{} {}: {}", statusCode, errClass, payload);
                } // else LoggerHandler will do log
            } else
                log.error("{}: {}", errClass, failure.getMessage(), failure);
        } // else LoggerHandler will do log
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ErrorLogHandler setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public int getWarnCode() {
        return warnCode;
    }

    public ErrorLogHandler setWarnCode(int warnCode) {
        this.warnCode = warnCode;
        return this;
    }

    public boolean isShowWarnStack() {
        return showWarnStack;
    }

    public ErrorLogHandler setShowWarnStack(boolean showWarnStack) {
        this.showWarnStack = showWarnStack;
        return this;
    }
}