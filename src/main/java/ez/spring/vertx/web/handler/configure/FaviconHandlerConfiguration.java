package ez.spring.vertx.web.handler.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.Nullable;

import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.ext.web.handler.FaviconHandler;

@Lazy
@Configuration
@ConfigurationProperties(VertxWebConfiguration.HANDLER_PREFIX + ".favicon-handler")
public class FaviconHandlerConfiguration extends AbstractHandlerConfiguration {
  private final String handler = FaviconHandler.class.getCanonicalName();
  private Integer order = 1000;
  private String path = "/favicon.ico";

  /**
   * @see FaviconHandler#DEFAULT_MAX_AGE_SECONDS
   */
  private long maxAgeSeconds = 86400L;
  /**
   * icon file path. if set to null, use `favicon.ico`(fallback to default icon in vertx jar if not found)
   */
  @Nullable
  private String iconFilePath;

  @Lazy
  @ConditionalOnMissingBean(FaviconHandler.class)
  @Bean
  public FaviconHandler faviconHandler() {
    return FaviconHandler.create(getIconFilePath(), getMaxAgeSeconds());
  }

  @Override
  public String getHandler() {
    return handler;
  }

  @Override
  public Integer getOrder() {
    return order;
  }

  @Override
  public FaviconHandlerConfiguration setOrder(Integer order) {
    this.order = order;
    return this;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public FaviconHandlerConfiguration setPath(String path) {
    this.path = path;
    return this;
  }

  public long getMaxAgeSeconds() {
    return maxAgeSeconds;
  }

  public FaviconHandlerConfiguration setMaxAgeSeconds(long maxAgeSeconds) {
    this.maxAgeSeconds = maxAgeSeconds;
    return this;
  }

  @Nullable
  public String getIconFilePath() {
    return iconFilePath;
  }

  public FaviconHandlerConfiguration setIconFilePath(@Nullable String iconFilePath) {
    this.iconFilePath = iconFilePath;
    return this;
  }
}