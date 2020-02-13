package ez.spring.vertx.web.handler.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ez.spring.vertx.web.VertxWebConfiguration;
import ez.spring.vertx.web.handler.MDCHandler;

@Lazy
@Configuration
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".mdc-handler")
public class MDCHandlerConfiguration extends AbstractHandlerConfiguration {
  private final String handler = MDCHandler.class.getCanonicalName();
  protected Integer order = -1000;
  private String mdcKey = MDCHandler.DEFAULT_MDC_KEY;

  @Override
  public Integer getOrder() {
    return order;
  }

  @Override
  public MDCHandlerConfiguration setOrder(Integer order) {
    this.order = order;
    return this;
  }

  @Override
  public String getHandler() {
    return handler;
  }

  @Lazy
  @ConditionalOnMissingBean(MDCHandler.class)
  @Bean
  public MDCHandler mdcHandler() {
    return new MDCHandler().setMdcKey(getMdcKey());
  }

  public String getMdcKey() {
    return mdcKey;
  }

  public MDCHandlerConfiguration setMdcKey(String mdcKey) {
    this.mdcKey = mdcKey;
    return this;
  }
}
