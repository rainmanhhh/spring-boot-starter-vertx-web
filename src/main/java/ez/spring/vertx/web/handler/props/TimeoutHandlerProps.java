package ez.spring.vertx.web.handler.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.vertx.ext.web.handler.TimeoutHandler;

@Component
@ConfigurationProperties("vertx.web.timeout-handler")
public class TimeoutHandlerProps {
    private long timeout = TimeoutHandler.DEFAULT_TIMEOUT;
    private int errorCode = TimeoutHandler.DEFAULT_ERRORCODE;

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
