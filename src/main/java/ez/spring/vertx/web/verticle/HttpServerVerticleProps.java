package ez.spring.vertx.web.verticle;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Collections;
import java.util.List;

import ez.spring.vertx.DeploymentOptionsEx;
import ez.spring.vertx.web.route.RouteProps;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class HttpServerVerticleProps {
    @NestedConfigurationProperty
    private DeploymentOptionsEx deploy = new DeploymentOptionsEx().setEnabled(false);
    private List<RouteProps> routes = Collections.emptyList();
}