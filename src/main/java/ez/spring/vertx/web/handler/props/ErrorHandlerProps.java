package ez.spring.vertx.web.handler.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.vertx.ext.web.common.WebEnvironment;
import io.vertx.ext.web.handler.ErrorHandler;

@Component
@ConfigurationProperties("vertx.web.error-handler")
public class ErrorHandlerProps {
    private String errorTemplateName = ErrorHandler.DEFAULT_ERROR_HANDLER_TEMPLATE;
    private boolean displayExceptionDetails = WebEnvironment.development();

    public String getErrorTemplateName() {
        return errorTemplateName;
    }

    public void setErrorTemplateName(String errorTemplateName) {
        this.errorTemplateName = errorTemplateName;
    }

    public boolean isDisplayExceptionDetails() {
        return displayExceptionDetails;
    }

    public void setDisplayExceptionDetails(boolean displayExceptionDetails) {
        this.displayExceptionDetails = displayExceptionDetails;
    }
}
