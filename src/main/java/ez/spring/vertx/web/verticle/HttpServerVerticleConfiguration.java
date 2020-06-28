package ez.spring.vertx.web.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
import ez.spring.vertx.deploy.DeployProps;
import ez.spring.vertx.http.HttpServerConfiguration;
import ez.spring.vertx.web.handler.configure.HandlerConfiguration;
import ez.spring.vertx.web.route.EzRouterUtil;
import ez.spring.vertx.web.route.RouteProps;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

import static ez.spring.vertx.http.HttpServerConfiguration.PREFIX;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Import(HttpServerConfiguration.class)
@ConfigurationProperties(PREFIX)
@Configuration
public class HttpServerVerticleConfiguration {
  private List<RouteProps> routes;
  private static final Logger log = LoggerFactory.getLogger(HttpServerVerticleConfiguration.class);

  @ConfigurationProperties(PREFIX + ".deploy")
  @Lazy
  @Bean
  public DeployProps httpServerVerticleDeploy(VertxProps vertxProps) {
    DeployProps vd = new DeployProps();
    vd.setDescriptor(HttpServerVerticle.class.getCanonicalName());
    vd.setInstances(vertxProps.getEventLoopPoolSize());
//    vd.setWorkerPoolSize(vertxProps.getWorkerPoolSize());
//    vd.setMaxWorkerExecuteTime(vertxProps.getMaxWorkerExecuteTime());
//    vd.setMaxWorkerExecuteTimeUnit(vertxProps.getMaxWorkerExecuteTimeUnit());
    return vd;
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
    EzRouterUtil.printRoutes(routes);
    return routes;
  }

  @Scope(SCOPE_PROTOTYPE)
  @Lazy
  @Bean
  public HttpServerVerticle httpServerVerticle(
    List<RouteProps> routes,
    HttpServerOptions httpServerOptions
  ) {
    return new HttpServerVerticle(httpServerOptions, routes);
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

  public List<RouteProps> getRoutes() {
    return routes;
  }

  public HttpServerVerticleConfiguration setRoutes(List<RouteProps> routes) {
    this.routes = routes;
    return this;
  }
}