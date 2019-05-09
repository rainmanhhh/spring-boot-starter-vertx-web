package ez.spring.vertx.web.handler.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import io.vertx.ext.web.common.WebEnvironment;
import io.vertx.ext.web.handler.TimeoutHandler;
import lombok.Data;

@Lazy
@Data
@Component
@ConfigurationProperties("vertx.web.timeout-handler")
public class TimeoutHandlerProps {
    private boolean enabled = !WebEnvironment.development();
    private long timeout = TimeoutHandler.DEFAULT_TIMEOUT;
    private int errorCode = TimeoutHandler.DEFAULT_ERRORCODE;
}
