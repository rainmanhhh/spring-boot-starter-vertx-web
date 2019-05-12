package ez.spring.vertx.web.handler.props;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.Data;

@Lazy
@Data
@Configuration
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".body-handler")
public class BodyHandlerProps extends AbstractHandlerProps {
    private final String handler = BodyHandler.class.getCanonicalName();
    private Integer order = -500;

    private boolean handleFileUploads = true;
    private String uploadDirectory = null;

    /**
     * @see BodyHandler#DEFAULT_MERGE_FORM_ATTRIBUTES
     */
    private boolean mergeFormAttributes = true;
    /**
     * @see BodyHandler#DEFAULT_DELETE_UPLOADED_FILES_ON_END
     */
    private boolean deleteUploadedFilesOnEnd = false;
    /**
     * @see BodyHandler#DEFAULT_PREALLOCATE_BODY_BUFFER
     */
    private boolean isPreallocateBodyBuffer = false;
    /**
     * @see BodyHandler#DEFAULT_BODY_LIMIT
     */
    private long bodyLimit = -1L;
    private Set<HttpMethod> methods = new HashSet<>(Arrays.asList(HttpMethod.POST, HttpMethod.PUT));

    @Lazy
    @ConditionalOnMissingBean(BodyHandler.class)
    @Bean
    public BodyHandler bodyHandler() {
        BodyHandler bodyHandler = getUploadDirectory() == null ?
                BodyHandler.create(isHandleFileUploads()) : BodyHandler.create(getUploadDirectory());
        bodyHandler.setBodyLimit(getBodyLimit());
        bodyHandler.setMergeFormAttributes(isMergeFormAttributes());
        bodyHandler.setDeleteUploadedFilesOnEnd(isDeleteUploadedFilesOnEnd());
        bodyHandler.setPreallocateBodyBuffer(isPreallocateBodyBuffer());
        return bodyHandler;
    }
}
