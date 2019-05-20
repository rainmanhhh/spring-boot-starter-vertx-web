package ez.spring.vertx.web.handler;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

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

    public String getContentType() {
        return contentType;
    }

    public OkHandler setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getBody() {
        return body;
    }

    public OkHandler setBody(String body) {
        this.body = body;
        return this;
    }
}