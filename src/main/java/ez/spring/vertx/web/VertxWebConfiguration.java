package ez.spring.vertx.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.List;

import ez.spring.vertx.VertxConfiguration;
import ez.spring.vertx.web.handler.OkHandler;
import ez.spring.vertx.web.handler.props.BodyHandlerProps;
import ez.spring.vertx.web.handler.props.ErrorHandlerProps;
import ez.spring.vertx.web.handler.props.LoggerHandlerProps;
import ez.spring.vertx.web.handler.props.StaticHandlerProps;
import ez.spring.vertx.web.handler.props.TimeoutHandlerProps;
import ez.spring.vertx.web.route.RouteMapper;
import ez.spring.vertx.web.route.props.RouteProps;
import io.vertx.core.Vertx;
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
        StaticHandlerProps.class
})
@Configuration
@ConfigurationProperties("vertx.web")
public class VertxWebConfiguration {
    private ApplicationContext applicationContext;

    public VertxWebConfiguration(@Autowired ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private List<RouteProps> routes = Collections.emptyList();

    public List<RouteProps> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteProps> routes) {
        this.routes = routes;
    }

    @Bean
    public Router router(Vertx vertx) {
        return Router.router(vertx);
    }

    @Bean
    public RouteMapper routeMapper(Router router) {
        return new RouteMapper(applicationContext, router, getRoutes());
    }

    @Bean
    public OkHandler okHandler() {
        return new OkHandler();
    }

    @ConditionalOnMissingBean(TimeoutHandler.class)
    @Bean
    public TimeoutHandler timeoutHandler(TimeoutHandlerProps props) {
        return TimeoutHandler.create(props.getTimeout(), props.getErrorCode());
    }

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

    @ConditionalOnMissingBean(ErrorHandler.class)
    @Bean
    public ErrorHandler errorHandler(ErrorHandlerProps props) {
        return ErrorHandler.create(props.getErrorTemplateName(), props.isDisplayExceptionDetails());
    }

    @ConditionalOnMissingBean(LoggerHandler.class)
    @Bean
    public LoggerHandler loggerHandler(LoggerHandlerProps props) {
        return LoggerHandler.create(props.isImmediate(), props.getLoggerFormat());
    }

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
}
