package ez.spring.vertx.web.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import org.springframework.lang.Nullable;

import java.util.Set;

public class OptionsHandler implements Handler<RoutingContext> {
    private final String allow;

    public OptionsHandler(@Nullable Set<HttpMethod> methods) {
        if (methods == null || methods.isEmpty()) allow = null;
        else {
            StringBuilder sb = new StringBuilder();
            for (HttpMethod method : methods) {
                sb.append(method).append(',');
            }
            allow = sb.substring(0, sb.length() - 1);
        }
    }

    @Override
    public void handle(RoutingContext event) {
        if (allow == null) event.response().end();
        else event.response()
                .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                .putHeader(HttpHeaders.ALLOW, allow)
                .end();
    }

    public String getAllow() {
        return allow;
    }
}
