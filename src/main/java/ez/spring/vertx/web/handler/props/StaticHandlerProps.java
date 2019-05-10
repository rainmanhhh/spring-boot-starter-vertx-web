package ez.spring.vertx.web.handler.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.ext.web.Http2PushMapping;
import io.vertx.ext.web.common.WebEnvironment;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.Data;
import lombok.experimental.Accessors;

@Lazy
@Accessors(chain = true)
@Data
@Component
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".static-handler")
public class StaticHandlerProps extends AbstractHandlerProps {
    private boolean enabled = false;
    private Integer order = 1100;
    private String handler = StaticHandler.class.getCanonicalName();
    private String path = "/static";
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
    private boolean cachingEnabled = !WebEnvironment.development();
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
}
