package ez.spring.vertx.web.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import ez.spring.vertx.VertxProps;
import ez.spring.vertx.deploy.DeploymentOptionsEx;
import ez.spring.vertx.deploy.VerticleDeploy;
import ez.spring.vertx.http.HttpServerConfiguration;
import ez.spring.vertx.web.VertxWebConfiguration;
import ez.spring.vertx.web.handler.configure.HandlerConfiguration;
import ez.spring.vertx.web.route.EzRouter;
import ez.spring.vertx.web.route.RouteProps;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Import(HttpServerConfiguration.class)
@Configuration
@ConfigurationProperties(HttpServerVerticleConfiguration.HTTP_SERVER_VERTICLE)
public class HttpServerVerticleConfiguration {
    static final String HTTP_SERVER_VERTICLE = VertxWebConfiguration.PREFIX + ".http-server-verticle";
    private final List<RouteProps> routes;
    @NestedConfigurationProperty
    private DeploymentOptionsEx deploy = new DeploymentOptionsEx().setEnabled(false);
    private Logger log = LoggerFactory.getLogger(getClass());

    public HttpServerVerticleConfiguration(List<RouteProps> routes) {
        this.routes = routes;
    }

    @Qualifier(HTTP_SERVER_VERTICLE)
    @Bean
    public VerticleDeploy httpServerVerticleDeploy(VertxProps vertxProps) {
        VerticleDeploy verticleDeploy = new VerticleDeploy(getDeploy());
        verticleDeploy
                .setDescriptor(HttpServerVerticle.class.getCanonicalName())
                .setInstances(vertxProps.getEventLoopPoolSize());
        return verticleDeploy;
    }

    @Lazy
    @Bean
    public Router router(Vertx vertx) {
        return Router.router(vertx);
    }

    @Lazy
    @Bean
    public List<RouteProps> mainRouteProps(
            ApplicationContext applicationContext,
            HttpServerVerticleConfiguration httpServerVerticleConfiguration
    ) {
        Collection<HandlerConfiguration> handlerProps = applicationContext.getBeansOfType(HandlerConfiguration.class).values();
        ArrayList<HandlerConfiguration> handlerConfigurationList = new ArrayList<>(handlerProps);
        handlerConfigurationList.sort(Comparator.comparing(HandlerConfiguration::getOrder));
        ArrayList<RouteProps> routes = new ArrayList<>();
        // add common routes whose order is null or less than 0
        for (HandlerConfiguration handlerConfiguration : handlerConfigurationList) {
            Integer order = handlerConfiguration.getOrder();
            if (handlerConfiguration.isEnabled() && (order == null || order < 0)) {
                routes.add(routeProps(handlerConfiguration));
            }
        }

        // add custom routes
        List<RouteProps> customRoutes = httpServerVerticleConfiguration.getRoutes();
        if (customRoutes != null && !customRoutes.isEmpty()) routes.addAll(customRoutes);

        // add common routes whose order greater than 0
        for (HandlerConfiguration handlerConfiguration : handlerConfigurationList) {
            Integer order = handlerConfiguration.getOrder();
            if (handlerConfiguration.isEnabled() && order != null && order > 0) {
                routes.add(routeProps(handlerConfiguration));
            }
        }

        log.info("mainRouteProps:");
        EzRouter.printRoutes(routes);
        return routes;
    }

    @Scope(SCOPE_PROTOTYPE)
    @Lazy
    @Bean
    public HttpServerVerticle httpServerVerticle(
            List<RouteProps> routes,
            HttpServerOptions httpServerOptions
    ) {
        return new HttpServerVerticle(httpServerOptions).setRoutes(routes);
    }

    private RouteProps routeProps(HandlerConfiguration hc) {
        return new RouteProps()
                .setPath(hc.getPath())
                .setOrder(hc.getOrder())
                .setMethods(hc.getMethods())
                .setHandler(hc.getHandler())
                .setErrorHandler(hc.getErrorHandler())
                .setWithOptionsHandler(hc.isWithOptionsHandler());
    }

    public DeploymentOptionsEx getDeploy() {
        return deploy;
    }

    public HttpServerVerticleConfiguration setDeploy(DeploymentOptionsEx deploy) {
        this.deploy = deploy;
        return this;
    }

    public List<RouteProps> getRoutes() {
        return routes;
    }
}