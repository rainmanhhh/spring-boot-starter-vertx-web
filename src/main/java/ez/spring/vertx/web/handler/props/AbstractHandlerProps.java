package ez.spring.vertx.web.handler.props;

import java.util.Set;

import io.vertx.core.http.HttpMethod;
import lombok.Data;

@Data
public abstract class AbstractHandlerProps implements HandlerProps {
    private boolean enabled = true;
    private final String errorHandler = null;
    private Integer order = null;
    private Set<HttpMethod> methods = null;
    private String path = null;
}
