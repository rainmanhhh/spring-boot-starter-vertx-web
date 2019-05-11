package ez.spring.vertx.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ez.spring.vertx.VertxConfiguration;
import ez.spring.vertx.httpServer.HttpServerConfiguration;
import ez.spring.vertx.web.handler.OkHandler;
import ez.spring.vertx.web.handler.props.BodyHandlerProps;
import ez.spring.vertx.web.handler.props.CorsHandlerProps;
import ez.spring.vertx.web.handler.props.ErrorHandlerProps;
import ez.spring.vertx.web.handler.props.FaviconHandlerProps;
import ez.spring.vertx.web.handler.props.LoggerHandlerProps;
import ez.spring.vertx.web.handler.props.StaticHandlerProps;
import ez.spring.vertx.web.handler.props.TimeoutHandlerProps;
import ez.spring.vertx.web.verticle.HttpServerVerticleProps;
import io.vertx.ext.web.common.WebEnvironment;

/**
 * when this config init, {@link WebEnvironment#mode()} value will be set
 * by spring active profile
 */
@Import({
        VertxConfiguration.class,
        HttpServerConfiguration.class,
        HttpServerVerticleProps.class,
        BodyHandlerProps.class,
        TimeoutHandlerProps.class,
        ErrorHandlerProps.class,
        LoggerHandlerProps.class,
        StaticHandlerProps.class,
        FaviconHandlerProps.class,
        CorsHandlerProps.class,
        OkHandler.class
})
@Configuration
@ConfigurationProperties(VertxWebConfiguration.PREFIX)
public class VertxWebConfiguration {
    public static final String PREFIX = VertxConfiguration.PREFIX + ".web";

    public VertxWebConfiguration(
            ApplicationContext applicationContext
    ) {
        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        for (String profile : activeProfiles) {
            if (profile.equalsIgnoreCase("dev") || profile.equalsIgnoreCase("Development")) {
                System.setProperty(WebEnvironment.SYSTEM_PROPERTY_NAME, profile);
                break;
            }
        }
        if (WebEnvironment.mode() == null && activeProfiles.length > 0) {
            System.setProperty(WebEnvironment.SYSTEM_PROPERTY_NAME, activeProfiles[0]);
        }
    }
}
