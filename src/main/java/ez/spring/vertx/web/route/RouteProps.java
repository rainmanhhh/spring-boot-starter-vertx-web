package ez.spring.vertx.web.route;

import java.util.Collections;
import java.util.List;

import io.vertx.core.http.HttpMethod;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class RouteProps {
    private String path;
    private List<HttpMethod> methods = Collections.emptyList();
    private String handler;
    private String errorHandler;
    private Integer order = null;
}
