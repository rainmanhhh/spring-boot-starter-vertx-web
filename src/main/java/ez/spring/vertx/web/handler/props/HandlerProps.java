package ez.spring.vertx.web.handler.props;

import java.util.List;

import io.vertx.core.http.HttpMethod;

public interface HandlerProps {
    boolean isEnabled();

    HandlerProps setEnabled(boolean enabled);

    /**
     * null means ignore it(use auto-incremental sequence)
     */
    Integer getOrder();

    /**
     * null means ignore it(use auto-incremental sequence)
     */
    HandlerProps setOrder(Integer order);

    List<HttpMethod> getMethods();

    HandlerProps setMethods(List<HttpMethod> methods);

    /**
     * @return handler classname
     */
    String getHandler();

    /**
     * @return handler classname
     */
    String getErrorHandler();

    /**
     *
     * @return null means `/*`
     */
    String getPath();

    /**
     *
     * @param path null means `/*`
     * @return this
     */
    HandlerProps setPath(String path);
}