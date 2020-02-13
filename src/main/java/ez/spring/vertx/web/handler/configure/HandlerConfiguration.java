package ez.spring.vertx.web.handler.configure;

import java.util.Set;

import io.vertx.core.http.HttpMethod;

public interface HandlerConfiguration {
  boolean isEnabled();

  HandlerConfiguration setEnabled(boolean enabled);

  /**
   * null means ignore it(use auto-incremental sequence)
   *
   * @return order
   */
  Integer getOrder();

  /**
   * null means ignore it(use auto-incremental sequence)
   *
   * @param order order value
   * @return this
   */
  HandlerConfiguration setOrder(Integer order);

  Set<HttpMethod> getMethods();

  HandlerConfiguration setMethods(Set<HttpMethod> methods);

  /**
   * @return handler classname
   */
  String getHandler();

  /**
   * @return handler classname
   */
  String getErrorHandler();

  /**
   * @return null means `/*`
   */
  String getPath();

  /**
   * @param path null means `/*`
   * @return this
   */
  HandlerConfiguration setPath(String path);

  boolean isWithOptionsHandler();

  HandlerConfiguration setWithOptionsHandler(boolean withOptionsHandler);
}