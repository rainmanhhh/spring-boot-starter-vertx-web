package ez.spring.vertx.web.handler.configure;

import java.util.Set;

import io.vertx.core.http.HttpMethod;

/**
 * orders:<br>
 * {@link TimeoutHandlerConfiguration}: -50000;<br>
 * {@link MDCHandlerConfiguration}: -1000;<br>
 * {@link CorsHandlerConfiguration}: -900;<br>
 * {@link LoggerHandlerConfiguration}: -800;<br>
 * {@link ErrorLogHandlerConfiguration}: -700;<br>
 * {@link SessionHandlerConfiguration}: -500;<br>
 * {@link BodyHandlerConfiguration}: -400;<br>
 * {@link FaviconHandlerConfiguration}: 1000;<br>
 * {@link StaticHandlerConfiguration}: 1100;<br>
 * {@link ErrorHandlerConfiguration}: 1200;<br>
 */
public abstract class AbstractHandlerConfiguration implements HandlerConfiguration {
  private boolean enabled = true;
  private Set<HttpMethod> methods = null;
  private String path = null;
  private boolean withOptionsHandler = false;

  @Override
  public String getErrorHandler() {
    return null;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public AbstractHandlerConfiguration setEnabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  @Override
  public Set<HttpMethod> getMethods() {
    return methods;
  }

  @Override
  public AbstractHandlerConfiguration setMethods(Set<HttpMethod> methods) {
    this.methods = methods;
    return this;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public AbstractHandlerConfiguration setPath(String path) {
    this.path = path;
    return this;
  }

  @Override
  public boolean isWithOptionsHandler() {
    return withOptionsHandler;
  }

  @Override
  public AbstractHandlerConfiguration setWithOptionsHandler(boolean withOptionsHandler) {
    this.withOptionsHandler = withOptionsHandler;
    return this;
  }
}
