package ez.spring.vertx.web.route;

import org.springframework.lang.Nullable;

import java.util.Set;

import io.vertx.core.http.HttpMethod;
import lombok.Data;

@Data
public class RouteProps {
    @Nullable
    private String path;
    @Nullable
    private Set<HttpMethod> methods = null;
    @Nullable
    private String handler;
    @Nullable
    private String errorHandler;
    @Nullable
    private Integer order = null;
    private boolean withOptionsHandler = false;
}
