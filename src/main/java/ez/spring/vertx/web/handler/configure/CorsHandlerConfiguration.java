package ez.spring.vertx.web.handler.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Set;

import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.handler.CorsHandler;

@Lazy
@Configuration
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".cors-handler")
public class CorsHandlerConfiguration extends AbstractHandlerConfiguration {
  private String handler = CorsHandler.class.getCanonicalName();
  private Integer order = -900;

  /**
   * regex pattern for http response header AllowedOrigin, or `*`;
   * if set to `*`, {@link #allowCredentials} cannot be true
   */
  private String allowedOriginPattern = "*";
  private Set<HttpMethod> allowedMethods = null;
  private Set<String> allowedHeaders = null;
  private Set<String> exposedHeaders = null;
  /**
   * if set to true, {@link #allowedOriginPattern} cannot be `*`
   */
  private boolean allowCredentials = false;
  /**
   * default value is 0(greater than 0 means valid)
   */
  private int maxAgeSeconds = 0;

  @Lazy
  @ConditionalOnMissingBean(CorsHandler.class)
  @Bean
  public CorsHandler corsHandler() {
    CorsHandler handler = CorsHandler.create(allowedOriginPattern);
    if (allowedMethods != null) handler.allowedMethods(allowedMethods);
    if (allowedHeaders != null) handler.allowedHeaders(allowedHeaders);
    if (exposedHeaders != null) handler.exposedHeaders(exposedHeaders);
    if (maxAgeSeconds > 0) handler.maxAgeSeconds(maxAgeSeconds);
    return handler.allowCredentials(allowCredentials);
  }

  @Override
  public String getHandler() {
    return handler;
  }

  public CorsHandlerConfiguration setHandler(String handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public Integer getOrder() {
    return order;
  }

  @Override
  public CorsHandlerConfiguration setOrder(Integer order) {
    this.order = order;
    return this;
  }

  public String getAllowedOriginPattern() {
    return allowedOriginPattern;
  }

  public CorsHandlerConfiguration setAllowedOriginPattern(String allowedOriginPattern) {
    this.allowedOriginPattern = allowedOriginPattern;
    return this;
  }

  public Set<HttpMethod> getAllowedMethods() {
    return allowedMethods;
  }

  public CorsHandlerConfiguration setAllowedMethods(Set<HttpMethod> allowedMethods) {
    this.allowedMethods = allowedMethods;
    return this;
  }

  public Set<String> getAllowedHeaders() {
    return allowedHeaders;
  }

  public CorsHandlerConfiguration setAllowedHeaders(Set<String> allowedHeaders) {
    this.allowedHeaders = allowedHeaders;
    return this;
  }

  public Set<String> getExposedHeaders() {
    return exposedHeaders;
  }

  public CorsHandlerConfiguration setExposedHeaders(Set<String> exposedHeaders) {
    this.exposedHeaders = exposedHeaders;
    return this;
  }

  public boolean isAllowCredentials() {
    return allowCredentials;
  }

  public CorsHandlerConfiguration setAllowCredentials(boolean allowCredentials) {
    this.allowCredentials = allowCredentials;
    return this;
  }

  public int getMaxAgeSeconds() {
    return maxAgeSeconds;
  }

  public CorsHandlerConfiguration setMaxAgeSeconds(int maxAgeSeconds) {
    this.maxAgeSeconds = maxAgeSeconds;
    return this;
  }
}