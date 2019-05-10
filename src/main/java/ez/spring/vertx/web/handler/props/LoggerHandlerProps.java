package ez.spring.vertx.web.handler.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;
import lombok.Data;
import lombok.experimental.Accessors;

@Lazy
@Accessors(chain = true)
@Data
@Component
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".log-handler")
public class LoggerHandlerProps extends AbstractHandlerProps {
    private String handler = LoggerHandler.class.getCanonicalName();
    private Integer order = -800;

    private boolean immediate = false;
    /**
     * @see LoggerFormat#DEFAULT
     */
    private LoggerFormat loggerFormat = LoggerFormat.DEFAULT;
}
