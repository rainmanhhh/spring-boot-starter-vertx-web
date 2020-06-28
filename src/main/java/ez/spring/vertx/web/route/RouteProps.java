package ez.spring.vertx.web.route;

import org.springframework.lang.Nullable;

import java.util.Set;

import io.vertx.core.http.HttpMethod;

@SuppressWarnings("WeakerAccess")
public class RouteProps {
  @Nullable
  private String path;
  @Nullable
  private Set<HttpMethod> methods = null;
  @Nullable
  private String subRouter;
  @Nullable
  private String handler;
  @Nullable
  private String errorHandler;
  @Nullable
  private Integer order = null;
  private boolean withOptionsHandler = false;

  @Nullable
  public String getSubRouter() {
    return subRouter;
  }

  public RouteProps setSubRouter(@Nullable String subRouter) {
    this.subRouter = subRouter;
    return this;
  }

  @Nullable
  public String getPath() {
    return path;
  }

  public RouteProps setPath(@Nullable String path) {
    this.path = path;
    return this;
  }

  @Nullable
  public Set<HttpMethod> getMethods() {
    return methods;
  }

  public RouteProps setMethods(@Nullable Set<HttpMethod> methods) {
    this.methods = methods;
    return this;
  }

  @Nullable
  public String getHandler() {
    return handler;
  }

  public RouteProps setHandler(@Nullable String handler) {
    this.handler = handler;
    return this;
  }

  @Nullable
  public String getErrorHandler() {
    return errorHandler;
  }

  public RouteProps setErrorHandler(@Nullable String errorHandler) {
    this.errorHandler = errorHandler;
    return this;
  }

  @Nullable
  public Integer getOrder() {
    return order;
  }

  public RouteProps setOrder(@Nullable Integer order) {
    this.order = order;
    return this;
  }

  public boolean isWithOptionsHandler() {
    return withOptionsHandler;
  }

  public RouteProps setWithOptionsHandler(boolean withOptionsHandler) {
    this.withOptionsHandler = withOptionsHandler;
    return this;
  }
}
