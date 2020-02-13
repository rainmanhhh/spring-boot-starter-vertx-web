package ez.spring.vertx.web.handler.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ez.spring.vertx.util.EzUtil;
import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.ext.web.handler.TimeoutHandler;

@Lazy
@Configuration
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".timeout-handler")
public class TimeoutHandlerConfiguration extends AbstractHandlerConfiguration {
  private boolean enabled = !EzUtil.getActiveProfiles().isDev();
  private Integer order = -50000;
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

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public TimeoutHandlerConfiguration setEnabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  @Override
  public Integer getOrder() {
    return order;
  }

  @Override
  public TimeoutHandlerConfiguration setOrder(Integer order) {
    this.order = order;
    return this;
  }

  @Override
  public String getHandler() {
    return handler;
  }

  public TimeoutHandlerConfiguration setHandler(String handler) {
    this.handler = handler;
    return this;
  }

  public long getTimeout() {
    return timeout;
  }

  public TimeoutHandlerConfiguration setTimeout(long timeout) {
    this.timeout = timeout;
    return this;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public TimeoutHandlerConfiguration setErrorCode(int errorCode) {
    this.errorCode = errorCode;
    return this;
  }
}