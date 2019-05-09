package ez.spring.vertx.web;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

import ez.spring.vertx.VerticleDeploy;
import ez.spring.vertx.VertxConfiguration;
import ez.spring.vertx.httpServer.HttpServerConfiguration;
import ez.spring.vertx.web.handler.OkHandler;
import ez.spring.vertx.web.handler.props.BodyHandlerProps;
import ez.spring.vertx.web.handler.props.ErrorHandlerProps;
import ez.spring.vertx.web.handler.props.LoggerHandlerProps;
import ez.spring.vertx.web.handler.props.StaticHandlerProps;
import ez.spring.vertx.web.handler.props.TimeoutHandlerProps;
import ez.spring.vertx.web.verticle.HttpServerVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TimeoutHandler;

@Import({
        VertxConfiguration.class,
        BodyHandlerProps.class,
        TimeoutHandlerProps.class,
        ErrorHandlerProps.class,
        LoggerHandlerProps.class,
        StaticHandlerProps.class,
        HttpServerConfiguration.class
})
@Configuration
@ConfigurationProperties("vertx.web")
public class VertxWebConfiguration {
    private static final String HTTP_SERVER_VERTICLE = "vertx.http-server-verticle";

    private final ApplicationContext applicationContext;
    private final Vertx vertx;

    public VertxWebConfiguration(
            ApplicationContext applicationContext,
            Vertx vertx
    ) {
        this.applicationContext = applicationContext;
        this.vertx = vertx;
    }

    @Lazy
    @Bean
    public Router router(Vertx vertx) {
        return Router.router(vertx);
    }

    @Lazy
    @Bean
    public OkHandler okHandler() {
        return new OkHandler();
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

    @ConfigurationProperties(HTTP_SERVER_VERTICLE + ".deploy")
    @Qualifier(HTTP_SERVER_VERTICLE)
    @Bean
    public VerticleDeploy httpServerVerticleProps() {
        VerticleDeploy deploy = new VerticleDeploy();
        deploy.setDescriptor(HttpServerVerticle.class.getCanonicalName());
        deploy.setEnabled(false);
        return deploy;
    }

    @Lazy
    @ConfigurationProperties(HTTP_SERVER_VERTICLE)
    @Bean
    public HttpServerVerticle httpServerVerticle(
            ApplicationContext applicationContext,
            HttpServer httpServer,
            Router router
    ) {
        return new HttpServerVerticle(applicationContext, httpServer, router);
    }
}
