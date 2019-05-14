package ez.spring.vertx.web.handler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ez.spring.vertx.ActiveProfiles;
import ez.spring.vertx.web.VertxWebConfiguration;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;

@Lazy
@Data
@ConfigurationProperties(VertxWebConfiguration.PREFIX + "error-log-handler")
@Configuration
public class ErrorLogHandlerConfiguration extends AbstractHandlerConfiguration {
    private String handler = null;
    private String errorHandler = ErrorLogHandler.class.getCanonicalName();
    private Integer order = 1200;

    /**
     * @see HttpResponseStatus#INTERNAL_SERVER_ERROR
     */
    private int errorCode = 500;
    /**
     * @see HttpResponseStatus#BAD_REQUEST
     */
    private int warnCode = 400;
    private boolean showWarnStack = ActiveProfiles.getInstance().isDev();

    @Lazy
    @ConditionalOnMissingBean(ErrorLogHandler.class)
    @Bean
    public ErrorLogHandler errorLogHandler() {
        return new ErrorLogHandler()
                .setErrorCode(getErrorCode())
                .setWarnCode(getWarnCode())
                .setShowWarnStack(isShowWarnStack());
    }
}
