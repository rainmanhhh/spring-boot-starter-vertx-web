package ez.spring.vertx.web.handler.props;

import java.util.Collections;
import java.util.List;

import io.vertx.core.http.HttpMethod;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public abstract class AbstractHandlerProps implements HandlerProps {
    private boolean enabled = true;
    private Integer order = null;
    private List<HttpMethod> methods = Collections.emptyList();
    private String path = null;
    private final String errorHandler = null;
}
