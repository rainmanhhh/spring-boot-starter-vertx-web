package ez.spring.vertx.web.handler.props;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;
import lombok.Data;

@Lazy
@Data
@Configuration
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".log-handler")
public class LoggerHandlerProps extends AbstractHandlerProps {
    private String handler = LoggerHandler.class.getCanonicalName();
    private Integer order = -800;

    private boolean immediate = false;
    /**
     * @see LoggerFormat#DEFAULT
     */
    private LoggerFormat loggerFormat = LoggerFormat.DEFAULT;

    @Lazy
    @ConditionalOnMissingBean(LoggerHandler.class)
    @Bean
    public LoggerHandler loggerHandler(LoggerHandlerProps props) {
        return LoggerHandler.create(props.isImmediate(), props.getLoggerFormat());
    }
}