package ez.spring.vertx.web.handler.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.vertx.ext.web.handler.BodyHandler;

@Component
@ConfigurationProperties("vertx.web.body-handler")
public class BodyHandlerProps {
    private boolean handleFileUploads = true;
    private String uploadDirectory = null;
    private boolean mergeFormAttributes = BodyHandler.DEFAULT_MERGE_FORM_ATTRIBUTES;
    private boolean deleteUploadedFilesOnEnd = BodyHandler.DEFAULT_DELETE_UPLOADED_FILES_ON_END;
    private boolean isPreallocateBodyBuffer = BodyHandler.DEFAULT_PREALLOCATE_BODY_BUFFER;
    private long bodyLimit = BodyHandler.DEFAULT_BODY_LIMIT;

    public boolean isMergeFormAttributes() {
        return mergeFormAttributes;
    }

    public void setMergeFormAttributes(boolean mergeFormAttributes) {
        this.mergeFormAttributes = mergeFormAttributes;
    }

    public boolean isDeleteUploadedFilesOnEnd() {
        return deleteUploadedFilesOnEnd;
    }

    public void setDeleteUploadedFilesOnEnd(boolean deleteUploadedFilesOnEnd) {
        this.deleteUploadedFilesOnEnd = deleteUploadedFilesOnEnd;
    }

    public boolean isPreallocateBodyBuffer() {
        return isPreallocateBodyBuffer;
    }

    public void setPreallocateBodyBuffer(boolean preallocateBodyBuffer) {
        isPreallocateBodyBuffer = preallocateBodyBuffer;
    }

    public long getBodyLimit() {
        return bodyLimit;
    }

    public void setBodyLimit(long bodyLimit) {
        this.bodyLimit = bodyLimit;
    }

    public boolean isHandleFileUploads() {
        return handleFileUploads;
    }

    public void setHandleFileUploads(boolean handleFileUploads) {
        this.handleFileUploads = handleFileUploads;
    }

    public String getUploadDirectory() {
        return uploadDirectory;
    }

    public void setUploadDirectory(String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
    }
}
