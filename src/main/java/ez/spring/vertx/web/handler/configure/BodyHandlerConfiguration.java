package ez.spring.vertx.web.handler.configure;

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

@Lazy
@Configuration
@ConfigurationProperties(VertxWebConfiguration.HANDLER_PREFIX + ".body-handler")
public class BodyHandlerConfiguration extends AbstractHandlerConfiguration {
  private final String handler = BodyHandler.class.getCanonicalName();
  private Integer order = -400;
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

  @Override
  public String getHandler() {
    return handler;
  }

  @Override
  public Integer getOrder() {
    return order;
  }

  @Override
  public BodyHandlerConfiguration setOrder(Integer order) {
    this.order = order;
    return this;
  }

  public boolean isHandleFileUploads() {
    return handleFileUploads;
  }

  public BodyHandlerConfiguration setHandleFileUploads(boolean handleFileUploads) {
    this.handleFileUploads = handleFileUploads;
    return this;
  }

  public String getUploadDirectory() {
    return uploadDirectory;
  }

  public BodyHandlerConfiguration setUploadDirectory(String uploadDirectory) {
    this.uploadDirectory = uploadDirectory;
    return this;
  }

  public boolean isMergeFormAttributes() {
    return mergeFormAttributes;
  }

  public BodyHandlerConfiguration setMergeFormAttributes(boolean mergeFormAttributes) {
    this.mergeFormAttributes = mergeFormAttributes;
    return this;
  }

  public boolean isDeleteUploadedFilesOnEnd() {
    return deleteUploadedFilesOnEnd;
  }

  public BodyHandlerConfiguration setDeleteUploadedFilesOnEnd(boolean deleteUploadedFilesOnEnd) {
    this.deleteUploadedFilesOnEnd = deleteUploadedFilesOnEnd;
    return this;
  }

  public boolean isPreallocateBodyBuffer() {
    return isPreallocateBodyBuffer;
  }

  public BodyHandlerConfiguration setPreallocateBodyBuffer(boolean preallocateBodyBuffer) {
    isPreallocateBodyBuffer = preallocateBodyBuffer;
    return this;
  }

  public long getBodyLimit() {
    return bodyLimit;
  }

  public BodyHandlerConfiguration setBodyLimit(long bodyLimit) {
    this.bodyLimit = bodyLimit;
    return this;
  }

  @Override
  public Set<HttpMethod> getMethods() {
    return methods;
  }

  @Override
  public BodyHandlerConfiguration setMethods(Set<HttpMethod> methods) {
    this.methods = methods;
    return this;
  }

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
