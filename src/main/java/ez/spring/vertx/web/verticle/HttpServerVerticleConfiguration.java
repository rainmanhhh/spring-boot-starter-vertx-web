package ez.spring.vertx.web.verticle;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ez.spring.vertx.deploy.DeploymentOptionsEx;
import ez.spring.vertx.deploy.VerticleDeploy;
import ez.spring.vertx.httpServer.HttpServerConfiguration;
import ez.spring.vertx.web.VertxWebConfiguration;
import ez.spring.vertx.web.handler.HandlerConfiguration;
import ez.spring.vertx.web.route.RouteProps;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Import(HttpServerConfiguration.class)
@Configuration
@ConfigurationProperties(HttpServerVerticleConfiguration.HTTP_SERVER_VERTICLE)
@Data
public class HttpServerVerticleConfiguration {
    static final String HTTP_SERVER_VERTICLE = VertxWebConfiguration.PREFIX + ".http-server-verticle";

    @NestedConfigurationProperty
    private DeploymentOptionsEx deploy = new DeploymentOptionsEx().setEnabled(false);
    private List<RouteProps> routes = Collections.emptyList();

    @Qualifier(HTTP_SERVER_VERTICLE)
    @Bean
    public VerticleDeploy httpServerVerticleDeploy(HttpServerVerticleConfiguration props) {
        return new VerticleDeploy(props.getDeploy()).setDescriptor(
                HttpServerVerticle.class.getCanonicalName()
        );
    }

    @Lazy
    @Bean
    public Router router(Vertx vertx) {
        return Router.router(vertx);
    }

    @Lazy
    @Bean
    public HttpServerVerticle httpServerVerticle(
            ApplicationContext applicationContext,
            HttpServerVerticleConfiguration httpServerVerticleConfiguration,
            HttpServer httpServer,
            Router router
    ) {
        Collection<HandlerConfiguration> handlerProps = applicationContext.getBeansOfType(HandlerConfiguration.class).values();
        ArrayList<HandlerConfiguration> handlerConfigurationList = new ArrayList<>(handlerProps);
        handlerConfigurationList.sort(Comparator.comparing(HandlerConfiguration::getOrder));
        ArrayList<RouteProps> routes = new ArrayList<>();
        // add common routes whose order is null or less than 0
        for (HandlerConfiguration props : handlerConfigurationList) {
            Integer order = props.getOrder();
            if (props.isEnabled() && (order == null || order < 0)) {
                routes.add(new RouteProps()
                        .setHandler(props.getHandler())
                        .setErrorHandler(props.getErrorHandler())
                        .setOrder(order)
                        .setMethods(props.getMethods())
                        .setPath(props.getPath())
                );
            }
        }

        // add custom routes
        List<RouteProps> customRoutes = httpServerVerticleConfiguration.getRoutes();
        if (customRoutes != null && !customRoutes.isEmpty()) routes.addAll(customRoutes);

        // add common routes whose order greater than 0
        for (HandlerConfiguration props : handlerConfigurationList) {
            Integer order = props.getOrder();
            if (props.isEnabled() && order != null && order > 0) {
                routes.add(new RouteProps()
                        .setHandler(props.getHandler())
                        .setErrorHandler(props.getErrorHandler())
                        .setOrder(order)
                        .setMethods(props.getMethods())
                        .setPath(props.getPath())
                );
            }
        }

        // set routes for httpServerVerticle
        return new HttpServerVerticle(applicationContext, httpServer, router).setRoutes(routes);
    }
}