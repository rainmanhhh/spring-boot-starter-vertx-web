package ez.spring.vertx.web.handler.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import io.vertx.ext.web.handler.BodyHandler;
import lombok.Data;

@Lazy
@Data
@Component
@ConfigurationProperties("vertx.web.body-handler")
public class BodyHandlerProps extends HandlerProps {
    private boolean enabled = true;
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
}
