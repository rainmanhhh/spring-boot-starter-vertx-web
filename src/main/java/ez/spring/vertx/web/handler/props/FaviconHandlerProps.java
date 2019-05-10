package ez.spring.vertx.web.handler.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.ext.web.handler.FaviconHandler;
import lombok.Data;
import lombok.experimental.Accessors;

@Lazy
@Accessors(chain = true)
@Data
@Component
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".favicon-handler")
public class FaviconHandlerProps extends AbstractHandlerProps {
    private final String handler = FaviconHandler.class.getCanonicalName();
    private Integer order = 1000;
    private String path = "/favicon.ico";

    /**
     * @see FaviconHandler#DEFAULT_MAX_AGE_SECONDS
     */
    private long maxAgeSeconds = 86400L;
    /**
     * icon file path. null means use default icon in vertx jar
     */
    private String iconFilePath;
}
