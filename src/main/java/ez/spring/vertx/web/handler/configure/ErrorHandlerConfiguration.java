package ez.spring.vertx.web.handler.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ez.spring.vertx.util.EzUtil;
import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.ext.web.handler.ErrorHandler;

@Lazy
@Configuration
@ConfigurationProperties(VertxWebConfiguration.HANDLER_PREFIX + ".error-handler")
public class ErrorHandlerConfiguration extends AbstractHandlerConfiguration {
  private final String errorHandler = ErrorHandler.class.getCanonicalName();
  private Integer order = 1200;
  /**
   * @see ErrorHandler#DEFAULT_ERROR_HANDLER_TEMPLATE
   */
  private String errorTemplateName = "META-INF/vertx/web/vertx-web-error.html";
  private boolean displayExceptionDetails = EzUtil.getActiveProfiles().isDev();

  @Lazy
  @ConditionalOnMissingBean(ErrorHandler.class)
  @Bean
  public ErrorHandler errorHandler() {
    return ErrorHandler.create(getErrorTemplateName(), isDisplayExceptionDetails());
  }

  @Override
  public Integer getOrder() {
    return order;
  }

  @Override
  public ErrorHandlerConfiguration setOrder(Integer order) {
    this.order = order;
    return this;
  }

  @Override
  public String getHandler() {
    return null;
  }

  @Override
  public String getErrorHandler() {
    return errorHandler;
  }

  public String getErrorTemplateName() {
    return errorTemplateName;
  }

  public ErrorHandlerConfiguration setErrorTemplateName(String errorTemplateName) {
    this.errorTemplateName = errorTemplateName;
    return this;
  }

  public boolean isDisplayExceptionDetails() {
    return displayExceptionDetails;
  }

  public ErrorHandlerConfiguration setDisplayExceptionDetails(boolean displayExceptionDetails) {
    this.displayExceptionDetails = displayExceptionDetails;
    return this;
  }
}
