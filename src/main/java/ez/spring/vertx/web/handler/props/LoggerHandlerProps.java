package ez.spring.vertx.web.handler.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.vertx.ext.web.handler.LoggerFormat;

@Component
@ConfigurationProperties("vertx.web.log-handler")
public class LoggerHandlerProps {
    public boolean isImmediate() {
        return immediate;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public LoggerFormat getLoggerFormat() {
        return loggerFormat;
    }

    public void setLoggerFormat(LoggerFormat loggerFormat) {
        this.loggerFormat = loggerFormat;
    }

    private boolean immediate = false;
    private LoggerFormat loggerFormat = LoggerFormat.DEFAULT;
}
