package ez.spring.vertx.web.handler.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import io.vertx.ext.web.common.WebEnvironment;
import io.vertx.ext.web.handler.ErrorHandler;
import lombok.Data;

@Lazy
@Data
@Component
@ConfigurationProperties("vertx.web.error-handler")
public class ErrorHandlerProps extends HandlerProps {
    private boolean enabled = true;
    /**
     * @see ErrorHandler#DEFAULT_ERROR_HANDLER_TEMPLATE
     */
    private String errorTemplateName = "META-INF/vertx/web/vertx-web-error.html";
    private boolean displayExceptionDetails = WebEnvironment.development();
}
