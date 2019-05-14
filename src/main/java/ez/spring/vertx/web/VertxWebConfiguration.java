package ez.spring.vertx.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ez.spring.vertx.ActiveProfiles;
import ez.spring.vertx.VertxConfiguration;
import ez.spring.vertx.httpServer.HttpServerConfiguration;
import ez.spring.vertx.web.handler.OkHandler;
import ez.spring.vertx.web.handler.configure.BodyHandlerConfiguration;
import ez.spring.vertx.web.handler.configure.CorsHandlerConfiguration;
import ez.spring.vertx.web.handler.configure.ErrorHandlerConfiguration;
import ez.spring.vertx.web.handler.configure.ErrorLogHandlerConfiguration;
import ez.spring.vertx.web.handler.configure.FaviconHandlerConfiguration;
import ez.spring.vertx.web.handler.configure.LoggerHandlerConfiguration;
import ez.spring.vertx.web.handler.configure.StaticHandlerConfiguration;
import ez.spring.vertx.web.handler.configure.TimeoutHandlerConfiguration;
import ez.spring.vertx.web.verticle.HttpServerVerticleConfiguration;
import io.vertx.ext.web.common.WebEnvironment;

/**
 * when this config init, {@link WebEnvironment#mode()} value will be set
 * by {@link ActiveProfiles}
 */
@Import({
        VertxConfiguration.class,
        HttpServerConfiguration.class,
        HttpServerVerticleConfiguration.class,
        BodyHandlerConfiguration.class,
        TimeoutHandlerConfiguration.class,
        ErrorLogHandlerConfiguration.class,
        ErrorHandlerConfiguration.class,
        LoggerHandlerConfiguration.class,
        StaticHandlerConfiguration.class,
        FaviconHandlerConfiguration.class,
        CorsHandlerConfiguration.class,
        OkHandler.class
})
@Configuration
@ConfigurationProperties(VertxWebConfiguration.PREFIX)
public class VertxWebConfiguration {
    public static final String PREFIX = VertxConfiguration.PREFIX + ".web";

    public VertxWebConfiguration() {
        ActiveProfiles activeProfiles = ActiveProfiles.getInstance();
        if (activeProfiles.isDev()) {
            System.setProperty(WebEnvironment.SYSTEM_PROPERTY_NAME, "dev");
        } else if (activeProfiles.isProd()) {
            System.setProperty(WebEnvironment.SYSTEM_PROPERTY_NAME, "prod");
        } else if (activeProfiles.isTest()) {
            System.setProperty(WebEnvironment.SYSTEM_PROPERTY_NAME, "test");
        }
    }
}
