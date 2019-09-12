package ez.spring.vertx.web.handler.configure;

import ez.spring.vertx.ActiveProfiles;
import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.ext.web.Http2PushMapping;
import io.vertx.ext.web.handler.StaticHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Lazy
@Configuration
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".static-handler")
public class StaticHandlerConfiguration extends AbstractHandlerConfiguration {
    private boolean enabled = false;
    private Integer order = 1100;
    private String handler = StaticHandler.class.getCanonicalName();
    private String path = "/static/*";
    /**
     * path based on process working dir
     *
     * @see StaticHandler#DEFAULT_WEB_ROOT
     */
    private String webRoot = "webroot";
    /**
     * @see StaticHandler#DEFAULT_ROOT_FILESYSTEM_ACCESS
     */
    private boolean allowRootFileSystemAccess = false;
    /**
     * @see StaticHandler#DEFAULT_FILES_READ_ONLY
     */
    private boolean readOnly = true;
    /**
     * @see StaticHandler#DEFAULT_MAX_AGE_SECONDS
     */
    private long maxAgeSeconds = 86400L;
    /**
     * @see StaticHandler#DEFAULT_CACHING_ENABLED
     */
    private boolean cachingEnabled = !ActiveProfiles.getInstance().isDev();
    /**
     * @see StaticHandler#DEFAULT_DIRECTORY_LISTING
     */
    private boolean directoryListing = false;
    /**
     * @see StaticHandler#DEFAULT_INCLUDE_HIDDEN
     */
    private boolean includeHidden = true;
    /**
     * @see StaticHandler#DEFAULT_CACHE_ENTRY_TIMEOUT
     */
    private long cacheEntryTimeout = 30000L;
    /**
     * @see StaticHandler#DEFAULT_INDEX_PAGE
     */
    private String indexPage = "/index.html";
    /**
     * @see StaticHandler#DEFAULT_MAX_CACHE_SIZE
     */
    private int maxCacheSize = 10000;
    private List<Http2PushMapping> http2PushMappings = Collections.emptyList();
    private Set<String> skipCompressionForMediaTypes = Collections.emptySet();
    private Set<String> skipCompressionForSuffixes = Collections.emptySet();
    /**
     * @see StaticHandler#DEFAULT_ALWAYS_ASYNC_FS
     */
    private boolean alwaysAsyncFS = false;
    /**
     * @see StaticHandler#DEFAULT_ENABLE_FS_TUNING
     */
    private boolean enableFSTuning = true;
    /**
     * @see StaticHandler#DEFAULT_MAX_AVG_SERVE_TIME_NS
     */
    private long maxAvgServeTimeNanoSeconds = 1_000_000L;
    /**
     * @see StaticHandler#DEFAULT_DIRECTORY_TEMPLATE
     */
    private String directoryTemplate = "META-INF/vertx/web/vertx-web-directory.html";
    /**
     * @see StaticHandler#DEFAULT_RANGE_SUPPORT
     */
    private boolean enableRangeSupport = true;
    /**
     * @see StaticHandler#DEFAULT_SEND_VARY_HEADER
     */
    private boolean varyHeader = true;
    private String defaultContentEncoding = StandardCharsets.UTF_8.name();

    @Lazy
    @ConditionalOnMissingBean(StaticHandler.class)
    @Bean
    public StaticHandler staticHandler() {
        return StaticHandler.create()
                .setAllowRootFileSystemAccess(isAllowRootFileSystemAccess())
                .setAlwaysAsyncFS(isAlwaysAsyncFS())
                .setCacheEntryTimeout(getCacheEntryTimeout())
                .setCachingEnabled(isCachingEnabled())
                .setDefaultContentEncoding(getDefaultContentEncoding())
                .setDirectoryListing(isDirectoryListing())
                .setDirectoryTemplate(getDirectoryTemplate())
                .setEnableFSTuning(isEnableFSTuning())
                .setEnableRangeSupport(isEnableRangeSupport())
                .setFilesReadOnly(isReadOnly())
                .setHttp2PushMapping(getHttp2PushMappings())
                .setIncludeHidden(isIncludeHidden())
                .setIndexPage(getIndexPage())
                .setMaxAgeSeconds(getMaxAgeSeconds())
                .setMaxAvgServeTimeNs(getMaxAvgServeTimeNanoSeconds())
                .setMaxCacheSize(getMaxCacheSize())
                .setSendVaryHeader(isVaryHeader())
                .setWebRoot(getWebRoot())
                .skipCompressionForMediaTypes(getSkipCompressionForMediaTypes())
                .skipCompressionForSuffixes(getSkipCompressionForSuffixes())
                ;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public StaticHandlerConfiguration setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public Integer getOrder() {
        return order;
    }

    @Override
    public StaticHandlerConfiguration setOrder(Integer order) {
        this.order = order;
        return this;
    }

    @Override
    public String getHandler() {
        return handler;
    }

    public StaticHandlerConfiguration setHandler(String handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public StaticHandlerConfiguration setPath(String path) {
        this.path = path;
        return this;
    }

    public String getWebRoot() {
        return webRoot;
    }

    public StaticHandlerConfiguration setWebRoot(String webRoot) {
        this.webRoot = webRoot;
        return this;
    }

    public boolean isAllowRootFileSystemAccess() {
        return allowRootFileSystemAccess;
    }

    public StaticHandlerConfiguration setAllowRootFileSystemAccess(boolean allowRootFileSystemAccess) {
        this.allowRootFileSystemAccess = allowRootFileSystemAccess;
        return this;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public StaticHandlerConfiguration setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    public long getMaxAgeSeconds() {
        return maxAgeSeconds;
    }

    public StaticHandlerConfiguration setMaxAgeSeconds(long maxAgeSeconds) {
        this.maxAgeSeconds = maxAgeSeconds;
        return this;
    }

    public boolean isCachingEnabled() {
        return cachingEnabled;
    }

    public StaticHandlerConfiguration setCachingEnabled(boolean cachingEnabled) {
        this.cachingEnabled = cachingEnabled;
        return this;
    }

    public boolean isDirectoryListing() {
        return directoryListing;
    }

    public StaticHandlerConfiguration setDirectoryListing(boolean directoryListing) {
        this.directoryListing = directoryListing;
        return this;
    }

    public boolean isIncludeHidden() {
        return includeHidden;
    }

    public StaticHandlerConfiguration setIncludeHidden(boolean includeHidden) {
        this.includeHidden = includeHidden;
        return this;
    }

    public long getCacheEntryTimeout() {
        return cacheEntryTimeout;
    }

    public StaticHandlerConfiguration setCacheEntryTimeout(long cacheEntryTimeout) {
        this.cacheEntryTimeout = cacheEntryTimeout;
        return this;
    }

    public String getIndexPage() {
        return indexPage;
    }

    public StaticHandlerConfiguration setIndexPage(String indexPage) {
        this.indexPage = indexPage;
        return this;
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    public StaticHandlerConfiguration setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
        return this;
    }

    public List<Http2PushMapping> getHttp2PushMappings() {
        return http2PushMappings;
    }

    public StaticHandlerConfiguration setHttp2PushMappings(List<Http2PushMapping> http2PushMappings) {
        this.http2PushMappings = http2PushMappings;
        return this;
    }

    public Set<String> getSkipCompressionForMediaTypes() {
        return skipCompressionForMediaTypes;
    }

    public StaticHandlerConfiguration setSkipCompressionForMediaTypes(Set<String> skipCompressionForMediaTypes) {
        this.skipCompressionForMediaTypes = skipCompressionForMediaTypes;
        return this;
    }

    public Set<String> getSkipCompressionForSuffixes() {
        return skipCompressionForSuffixes;
    }

    public StaticHandlerConfiguration setSkipCompressionForSuffixes(Set<String> skipCompressionForSuffixes) {
        this.skipCompressionForSuffixes = skipCompressionForSuffixes;
        return this;
    }

    public boolean isAlwaysAsyncFS() {
        return alwaysAsyncFS;
    }

    public StaticHandlerConfiguration setAlwaysAsyncFS(boolean alwaysAsyncFS) {
        this.alwaysAsyncFS = alwaysAsyncFS;
        return this;
    }

    public boolean isEnableFSTuning() {
        return enableFSTuning;
    }

    public StaticHandlerConfiguration setEnableFSTuning(boolean enableFSTuning) {
        this.enableFSTuning = enableFSTuning;
        return this;
    }

    public long getMaxAvgServeTimeNanoSeconds() {
        return maxAvgServeTimeNanoSeconds;
    }

    public StaticHandlerConfiguration setMaxAvgServeTimeNanoSeconds(long maxAvgServeTimeNanoSeconds) {
        this.maxAvgServeTimeNanoSeconds = maxAvgServeTimeNanoSeconds;
        return this;
    }

    public String getDirectoryTemplate() {
        return directoryTemplate;
    }

    public StaticHandlerConfiguration setDirectoryTemplate(String directoryTemplate) {
        this.directoryTemplate = directoryTemplate;
        return this;
    }

    public boolean isEnableRangeSupport() {
        return enableRangeSupport;
    }

    public StaticHandlerConfiguration setEnableRangeSupport(boolean enableRangeSupport) {
        this.enableRangeSupport = enableRangeSupport;
        return this;
    }

    public boolean isVaryHeader() {
        return varyHeader;
    }

    public StaticHandlerConfiguration setVaryHeader(boolean varyHeader) {
        this.varyHeader = varyHeader;
        return this;
    }

    public String getDefaultContentEncoding() {
        return defaultContentEncoding;
    }

    public StaticHandlerConfiguration setDefaultContentEncoding(String defaultContentEncoding) {
        this.defaultContentEncoding = defaultContentEncoding;
        return this;
    }
}
