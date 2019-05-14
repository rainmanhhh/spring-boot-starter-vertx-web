package ez.spring.vertx.web.handler.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ez.spring.vertx.ActiveProfiles;
import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.ext.web.handler.ErrorHandler;
import lombok.Data;

@Lazy
@Data
@Configuration
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".error-handler")
public class ErrorHandlerConfiguration extends AbstractHandlerConfiguration {
    private Integer order = 1200;
    private final String handler = null;
    private final String errorHandler = ErrorHandler.class.getCanonicalName();

    /**
     * @see ErrorHandler#DEFAULT_ERROR_HANDLER_TEMPLATE
     */
    private String errorTemplateName = "META-INF/vertx/web/vertx-web-error.html";
    private boolean displayExceptionDetails = ActiveProfiles.getInstance().isDev();

    @Lazy
    @ConditionalOnMissingBean(ErrorHandler.class)
    @Bean
    public ErrorHandler errorHandler() {
        return ErrorHandler.create(getErrorTemplateName(), isDisplayExceptionDetails());
    }
}
