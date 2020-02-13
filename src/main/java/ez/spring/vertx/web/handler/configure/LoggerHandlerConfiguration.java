package ez.spring.vertx.web.handler.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;

@Lazy
@Configuration
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".log-handler")
public class LoggerHandlerConfiguration extends AbstractHandlerConfiguration {
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
  public LoggerHandler loggerHandler(LoggerHandlerConfiguration props) {
    return LoggerHandler.create(props.isImmediate(), props.getLoggerFormat());
  }

  @Override
  public String getHandler() {
    return handler;
  }

  public LoggerHandlerConfiguration setHandler(String handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public Integer getOrder() {
    return order;
  }

  @Override
  public LoggerHandlerConfiguration setOrder(Integer order) {
    this.order = order;
    return this;
  }

  public boolean isImmediate() {
    return immediate;
  }

  public LoggerHandlerConfiguration setImmediate(boolean immediate) {
    this.immediate = immediate;
    return this;
  }

  public LoggerFormat getLoggerFormat() {
    return loggerFormat;
  }

  public LoggerHandlerConfiguration setLoggerFormat(LoggerFormat loggerFormat) {
    this.loggerFormat = loggerFormat;
    return this;
  }
}