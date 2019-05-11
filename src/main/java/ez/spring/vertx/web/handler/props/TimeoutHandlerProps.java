package ez.spring.vertx.web.handler.props;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.ext.web.common.WebEnvironment;
import io.vertx.ext.web.handler.TimeoutHandler;
import lombok.Data;

@Lazy
@Data
@Configuration
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".timeout-handler")
public class TimeoutHandlerProps extends AbstractHandlerProps {
    private boolean enabled = !WebEnvironment.development();
    private Integer order = -1000;
    private String handler = TimeoutHandler.class.getCanonicalName();

    /**
     * @see TimeoutHandler#DEFAULT_TIMEOUT
     */
    private long timeout = 5000L;
    /**
     * @see TimeoutHandler#DEFAULT_ERRORCODE
     */
    private int errorCode = 503;

    @Lazy
    @ConditionalOnMissingBean(TimeoutHandler.class)
    @Bean
    public TimeoutHandler timeoutHandler() {
        return TimeoutHandler.create(getTimeout(), getErrorCode());
    }

}