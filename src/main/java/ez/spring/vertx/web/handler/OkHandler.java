package ez.spring.vertx.web.handler;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.experimental.Accessors;

@Lazy
@Component
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".ok-handler")
@Accessors(chain = true)
@Data
public class OkHandler implements Handler<RoutingContext> {
    private String contentType;
    private String body;

    @Override
    public void handle(RoutingContext event) {
        HttpServerResponse response = event.response();
        if (contentType != null) response.putHeader(HttpHeaders.CONTENT_TYPE, contentType);
        if (body != null) response.end(body);
        else response.end();
    }
}
