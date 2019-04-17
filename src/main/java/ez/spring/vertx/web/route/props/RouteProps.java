package ez.spring.vertx.web.route.props;

import java.util.Collections;
import java.util.List;

import io.vertx.core.http.HttpMethod;

public class RouteProps {
    private String path;
    private List<HttpMethod> methods = Collections.emptyList();
    private String handler;
    private String errorHandler;

    public String getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(String errorHandler) {
        this.errorHandler = errorHandler;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<HttpMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<HttpMethod> methods) {
        this.methods = methods;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }
}
