package ez.spring.vertx.web.handler.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.ext.web.common.WebEnvironment;
import io.vertx.ext.web.handler.ErrorHandler;
import lombok.Data;
import lombok.experimental.Accessors;

@Lazy
@Accessors(chain = true)
@Data
@Component
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".error-handler")
public class ErrorHandlerProps extends AbstractHandlerProps {
    private Integer order = 1200;
    private final String handler = null;
    private final String errorHandler = ErrorHandler.class.getCanonicalName();

    /**
     * @see ErrorHandler#DEFAULT_ERROR_HANDLER_TEMPLATE
     */
    private String errorTemplateName = "META-INF/vertx/web/vertx-web-error.html";
    private boolean displayExceptionDetails = WebEnvironment.development();
}
