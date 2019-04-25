package ez.spring.vertx.web.handler.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import io.vertx.ext.web.Http2PushMapping;
import io.vertx.ext.web.handler.StaticHandler;

@Component
@ConfigurationProperties("vertx.web.static-handler")
public class StaticHandlerProps {
    private String webRoot = StaticHandler.DEFAULT_WEB_ROOT;
    private boolean allowRootFileSystemAccess = StaticHandler.DEFAULT_ROOT_FILESYSTEM_ACCESS;
    private boolean readOnly = StaticHandler.DEFAULT_FILES_READ_ONLY;
    private long maxAgeSeconds = StaticHandler.DEFAULT_MAX_AGE_SECONDS;
    private boolean cachingEnabled = StaticHandler.DEFAULT_CACHING_ENABLED;
    private boolean directoryListing = StaticHandler.DEFAULT_DIRECTORY_LISTING;
    private boolean includeHidden = StaticHandler.DEFAULT_INCLUDE_HIDDEN;
    private long cacheEntryTimeout = StaticHandler.DEFAULT_CACHE_ENTRY_TIMEOUT;
    private String indexPage = StaticHandler.DEFAULT_INDEX_PAGE;
    private int maxCacheSize = StaticHandler.DEFAULT_MAX_CACHE_SIZE;
    private List<Http2PushMapping> http2PushMappings = Collections.emptyList();
    private Set<String> skipCompressionForMediaTypes = Collections.emptySet();
    private Set<String> skipCompressionForSuffixes = Collections.emptySet();
    private boolean alwaysAsyncFS = StaticHandler.DEFAULT_ALWAYS_ASYNC_FS;
    private boolean enableFSTuning = StaticHandler.DEFAULT_ENABLE_FS_TUNING;
    private long maxAvgServeTimeNanoSeconds = StaticHandler.DEFAULT_MAX_AVG_SERVE_TIME_NS;
    private String directoryTemplate = StaticHandler.DEFAULT_DIRECTORY_TEMPLATE;
    private boolean enableRangeSupport = StaticHandler.DEFAULT_RANGE_SUPPORT;
    private boolean varyHeader = StaticHandler.DEFAULT_SEND_VARY_HEADER;
    private String defaultContentEncoding = StandardCharsets.UTF_8.name();

    public String getWebRoot() {
        return webRoot;
    }

    public void setWebRoot(String webRoot) {
        this.webRoot = webRoot;
    }

    public boolean isAllowRootFileSystemAccess() {
        return allowRootFileSystemAccess;
    }

    public void setAllowRootFileSystemAccess(boolean allowRootFileSystemAccess) {
        this.allowRootFileSystemAccess = allowRootFileSystemAccess;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public long getMaxAgeSeconds() {
        return maxAgeSeconds;
    }

    public void setMaxAgeSeconds(long maxAgeSeconds) {
        this.maxAgeSeconds = maxAgeSeconds;
    }

    public boolean isCachingEnabled() {
        return cachingEnabled;
    }

    public void setCachingEnabled(boolean cachingEnabled) {
        this.cachingEnabled = cachingEnabled;
    }

    public boolean isDirectoryListing() {
        return directoryListing;
    }

    public void setDirectoryListing(boolean directoryListing) {
        this.directoryListing = directoryListing;
    }

    public boolean isIncludeHidden() {
        return includeHidden;
    }

    public void setIncludeHidden(boolean includeHidden) {
        this.includeHidden = includeHidden;
    }

    public long getCacheEntryTimeout() {
        return cacheEntryTimeout;
    }

    public void setCacheEntryTimeout(long cacheEntryTimeout) {
        this.cacheEntryTimeout = cacheEntryTimeout;
    }

    public String getIndexPage() {
        return indexPage;
    }

    public void setIndexPage(String indexPage) {
        this.indexPage = indexPage;
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public List<Http2PushMapping> getHttp2PushMappings() {
        return http2PushMappings;
    }

    public void setHttp2PushMappings(List<Http2PushMapping> http2PushMappings) {
        this.http2PushMappings = http2PushMappings;
    }

    public Set<String> getSkipCompressionForMediaTypes() {
        return skipCompressionForMediaTypes;
    }

    public void setSkipCompressionForMediaTypes(Set<String> skipCompressionForMediaTypes) {
        this.skipCompressionForMediaTypes = skipCompressionForMediaTypes;
    }

    public Set<String> getSkipCompressionForSuffixes() {
        return skipCompressionForSuffixes;
    }

    public void setSkipCompressionForSuffixes(Set<String> skipCompressionForSuffixes) {
        this.skipCompressionForSuffixes = skipCompressionForSuffixes;
    }

    public boolean isAlwaysAsyncFS() {
        return alwaysAsyncFS;
    }

    public void setAlwaysAsyncFS(boolean alwaysAsyncFS) {
        this.alwaysAsyncFS = alwaysAsyncFS;
    }

    public boolean isEnableFSTuning() {
        return enableFSTuning;
    }

    public void setEnableFSTuning(boolean enableFSTuning) {
        this.enableFSTuning = enableFSTuning;
    }

    public long getMaxAvgServeTimeNanoSeconds() {
        return maxAvgServeTimeNanoSeconds;
    }

    public void setMaxAvgServeTimeNanoSeconds(long maxAvgServeTimeNanoSeconds) {
        this.maxAvgServeTimeNanoSeconds = maxAvgServeTimeNanoSeconds;
    }

    public String getDirectoryTemplate() {
        return directoryTemplate;
    }

    public void setDirectoryTemplate(String directoryTemplate) {
        this.directoryTemplate = directoryTemplate;
    }

    public boolean isEnableRangeSupport() {
        return enableRangeSupport;
    }

    public void setEnableRangeSupport(boolean enableRangeSupport) {
        this.enableRangeSupport = enableRangeSupport;
    }

    public boolean isVaryHeader() {
        return varyHeader;
    }

    public void setVaryHeader(boolean varyHeader) {
        this.varyHeader = varyHeader;
    }

    public String getDefaultContentEncoding() {
        return defaultContentEncoding;
    }

    public void setDefaultContentEncoding(String defaultContentEncoding) {
        this.defaultContentEncoding = defaultContentEncoding;
    }
}
