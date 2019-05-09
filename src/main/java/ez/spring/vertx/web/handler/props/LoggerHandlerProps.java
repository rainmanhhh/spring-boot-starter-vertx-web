package ez.spring.vertx.web.handler.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import io.vertx.ext.web.handler.LoggerFormat;
import lombok.Data;

@Lazy
@Data
@Component
@ConfigurationProperties("vertx.web.log-handler")
public class LoggerHandlerProps extends HandlerProps {
    private boolean enabled = true;
    private boolean immediate = false;
    /**
     * @see LoggerFormat#DEFAULT
     */
    private LoggerFormat loggerFormat = LoggerFormat.DEFAULT;
}
