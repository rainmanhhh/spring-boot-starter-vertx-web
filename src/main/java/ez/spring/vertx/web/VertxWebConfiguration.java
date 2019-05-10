package ez.spring.vertx.web;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import ez.spring.vertx.VerticleDeploy;
import ez.spring.vertx.VertxConfiguration;
import ez.spring.vertx.httpServer.HttpServerConfiguration;
import ez.spring.vertx.web.handler.OkHandler;
import ez.spring.vertx.web.handler.props.BodyHandlerProps;
import ez.spring.vertx.web.handler.props.ErrorHandlerProps;
import ez.spring.vertx.web.handler.props.FaviconHandlerProps;
import ez.spring.vertx.web.handler.props.HandlerProps;
import ez.spring.vertx.web.handler.props.LoggerHandlerProps;
import ez.spring.vertx.web.handler.props.StaticHandlerProps;
import ez.spring.vertx.web.handler.props.TimeoutHandlerProps;
import ez.spring.vertx.web.route.RouteProps;
import ez.spring.vertx.web.verticle.HttpServerVerticle;
import ez.spring.vertx.web.verticle.HttpServerVerticleProps;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.common.WebEnvironment;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.FaviconHandler;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TimeoutHandler;

/**
 * when this config init, {@link WebEnvironment#mode()} value will be set
 * by spring active profile
 */
@Import({
        VertxConfiguration.class,
        HttpServerConfiguration.class,
        BodyHandlerProps.class,
        TimeoutHandlerProps.class,
        ErrorHandlerProps.class,
        LoggerHandlerProps.class,
        StaticHandlerProps.class,
        FaviconHandlerProps.class,
        OkHandler.class
})
@Configuration
@ConfigurationProperties(VertxWebConfiguration.PREFIX)
public class VertxWebConfiguration {
    public static final String PREFIX = VertxConfiguration.PREFIX + ".web";
    private static final String HTTP_SERVER_VERTICLE = PREFIX + ".http-server-verticle";

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

    @Lazy
    @Bean
    public Router router(Vertx vertx) {
        return Router.router(vertx);
    }

    @Lazy
    @ConditionalOnMissingBean(TimeoutHandler.class)
    @Bean
    public TimeoutHandler timeoutHandler(TimeoutHandlerProps props) {
        return TimeoutHandler.create(props.getTimeout(), props.getErrorCode());
    }

    @Lazy
    @ConditionalOnMissingBean(BodyHandler.class)
    @Bean
    public BodyHandler bodyHandler(BodyHandlerProps props) {
        BodyHandler bodyHandler = props.getUploadDirectory() == null ?
                BodyHandler.create(props.isHandleFileUploads()) : BodyHandler.create(props.getUploadDirectory());
        bodyHandler.setBodyLimit(props.getBodyLimit());
        bodyHandler.setMergeFormAttributes(props.isMergeFormAttributes());
        bodyHandler.setDeleteUploadedFilesOnEnd(props.isDeleteUploadedFilesOnEnd());
        bodyHandler.setPreallocateBodyBuffer(props.isPreallocateBodyBuffer());
        return bodyHandler;
    }

    @Lazy
    @ConditionalOnMissingBean(ErrorHandler.class)
    @Bean
    public ErrorHandler errorHandler(ErrorHandlerProps props) {
        return ErrorHandler.create(props.getErrorTemplateName(), props.isDisplayExceptionDetails());
    }

    @Lazy
    @ConditionalOnMissingBean(LoggerHandler.class)
    @Bean
    public LoggerHandler loggerHandler(LoggerHandlerProps props) {
        return LoggerHandler.create(props.isImmediate(), props.getLoggerFormat());
    }

    @Lazy
    @ConditionalOnMissingBean(FaviconHandler.class)
    @Bean
    public FaviconHandler faviconHandler(FaviconHandlerProps props) {
        return FaviconHandler.create(props.getPath(), props.getMaxAgeSeconds());
    }

    @Lazy
    @ConditionalOnMissingBean(StaticHandler.class)
    @Bean
    public StaticHandler staticHandler(StaticHandlerProps props) {
        return StaticHandler.create()
                .setAllowRootFileSystemAccess(props.isAllowRootFileSystemAccess())
                .setAlwaysAsyncFS(props.isAlwaysAsyncFS())
                .setCacheEntryTimeout(props.getCacheEntryTimeout())
                .setCachingEnabled(props.isCachingEnabled())
                .setDefaultContentEncoding(props.getDefaultContentEncoding())
                .setDirectoryListing(props.isDirectoryListing())
                .setDirectoryTemplate(props.getDirectoryTemplate())
                .setEnableFSTuning(props.isEnableFSTuning())
                .setEnableRangeSupport(props.isEnableRangeSupport())
                .setFilesReadOnly(props.isReadOnly())
                .setHttp2PushMapping(props.getHttp2PushMappings())
                .setIncludeHidden(props.isIncludeHidden())
                .setIndexPage(props.getIndexPage())
                .setMaxAgeSeconds(props.getMaxAgeSeconds())
                .setMaxAvgServeTimeNs(props.getMaxAvgServeTimeNanoSeconds())
                .setMaxCacheSize(props.getMaxCacheSize())
                .setSendVaryHeader(props.isVaryHeader())
                .setWebRoot(props.getWebRoot())
                .skipCompressionForMediaTypes(props.getSkipCompressionForMediaTypes())
                .skipCompressionForSuffixes(props.getSkipCompressionForSuffixes())
                ;
    }

    @Qualifier(HTTP_SERVER_VERTICLE)
    @Bean
    public VerticleDeploy httpServerVerticleDeploy(HttpServerVerticleProps props) {
        VerticleDeploy verticleDeploy = new VerticleDeploy(props.getDeploy());
        verticleDeploy.setDescriptor(HttpServerVerticle.class.getCanonicalName());
        return verticleDeploy;
    }

    @ConfigurationProperties(HTTP_SERVER_VERTICLE)
    @Bean
    public HttpServerVerticleProps httpServerVerticleProps() {
        return new HttpServerVerticleProps();
    }

    @Lazy
    @Bean
    public HttpServerVerticle httpServerVerticle(
            ApplicationContext applicationContext,
            HttpServerVerticleProps httpServerVerticleProps,
            HttpServer httpServer,
            Router router //,
//            BodyHandlerProps bodyHandlerProps,
//            ErrorHandlerProps errorHandlerProps,
//            FaviconHandlerProps faviconHandlerProps,
//            LoggerHandlerProps loggerHandlerProps,
//            StaticHandlerProps staticHandlerProps,
//            TimeoutHandlerProps timeoutHandlerProps
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
