package ez.spring.vertx.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ez.spring.vertx.ActiveProfiles;
import ez.spring.vertx.http.HttpServerConfiguration;
import ez.spring.vertx.util.EzUtil;
import ez.spring.vertx.web.handler.OkHandler;
import io.vertx.ext.web.common.WebEnvironment;

/**
 * when this config init, {@link WebEnvironment#mode()} value will be set
 * by {@link ActiveProfiles}
 */
@Configuration
public class VertxWebConfiguration {
  public static final String HANDLER_PREFIX = HttpServerConfiguration.PREFIX + ".handlers";

  public VertxWebConfiguration() {
    ActiveProfiles activeProfiles = EzUtil.getActiveProfiles();
    if (activeProfiles.isDev()) {
      System.setProperty(WebEnvironment.SYSTEM_PROPERTY_NAME, "dev");
    } else if (activeProfiles.isProd()) {
      System.setProperty(WebEnvironment.SYSTEM_PROPERTY_NAME, "prod");
    } else if (activeProfiles.isTest()) {
      System.setProperty(WebEnvironment.SYSTEM_PROPERTY_NAME, "test");
    }
  }

  @Lazy
  @ConfigurationProperties(HANDLER_PREFIX + ".ok-handler")
  @Bean
  public OkHandler okHandler() {
    return new OkHandler();
  }
}