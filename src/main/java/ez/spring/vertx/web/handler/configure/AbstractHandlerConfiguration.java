package ez.spring.vertx.web.handler.configure;

import java.util.Set;

import io.vertx.core.http.HttpMethod;
import lombok.Data;

@Data
public abstract class AbstractHandlerConfiguration implements HandlerConfiguration {
    private boolean enabled = true;
    private final String errorHandler = null;
    private Integer order = null;
    private Set<HttpMethod> methods = null;
    private String path = null;
    private boolean withOptionsHandler = false;
}
