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

import ez.spring.vertx.DeploymentOptionsEx;
import ez.spring.vertx.VerticleDeploy;
import ez.spring.vertx.httpServer.HttpServerConfiguration;
import ez.spring.vertx.web.VertxWebConfiguration;
import ez.spring.vertx.web.handler.props.HandlerProps;
import ez.spring.vertx.web.route.RouteProps;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import lombok.Data;

@Import(HttpServerConfiguration.class)
@Configuration
@ConfigurationProperties(HttpServerVerticleProps.HTTP_SERVER_VERTICLE)
@Data
public class HttpServerVerticleProps {
    static final String HTTP_SERVER_VERTICLE = VertxWebConfiguration.PREFIX + ".http-server-verticle";

    @NestedConfigurationProperty
    private DeploymentOptionsEx deploy = new DeploymentOptionsEx().setEnabled(false);
    private List<RouteProps> routes = Collections.emptyList();

    @Qualifier(HTTP_SERVER_VERTICLE)
    @Bean
    public VerticleDeploy httpServerVerticleDeploy(HttpServerVerticleProps props) {
        VerticleDeploy verticleDeploy = new VerticleDeploy(props.getDeploy());
        verticleDeploy.setDescriptor(HttpServerVerticle.class.getCanonicalName());
        return verticleDeploy;
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
            HttpServerVerticleProps httpServerVerticleProps,
            HttpServer httpServer,
            Router router
    ) {
        Collection<HandlerProps> handlerProps = applicationContext.getBeansOfType(HandlerProps.class).values();
        ArrayList<HandlerProps> handlerPropsList = new ArrayList<>(handlerProps);
        handlerPropsList.sort(Comparator.comparing(HandlerProps::getOrder));
        ArrayList<RouteProps> routes = new ArrayList<>();
        // add common routes order is null or less than 0
        for (HandlerProps props : handlerPropsList) {
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
        List<RouteProps> customRoutes = httpServerVerticleProps.getRoutes();
        if (customRoutes != null) routes.addAll(customRoutes);

        // add common routes order greater than 0
        for (HandlerProps props : handlerPropsList) {
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
        HttpServerVerticle httpServerVerticle = new HttpServerVerticle(applicationContext, httpServer, router);
        httpServerVerticle.setRoutes(routes);
        return httpServerVerticle;
    }
}