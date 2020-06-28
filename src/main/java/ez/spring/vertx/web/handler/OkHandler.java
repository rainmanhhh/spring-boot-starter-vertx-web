package ez.spring.vertx.web.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class OkHandler implements Handler<RoutingContext> {
  private String contentType;
  private String body;
  /**
   * http status code for no content(which means body is null)
   *
   * @see HttpResponseStatus#NO_CONTENT
   */
  private int noContentCode = HttpResponseStatus.NO_CONTENT.code();

  public int getNoContentCode() {
    return noContentCode;
  }

  public OkHandler setNoContentCode(int noContentCode) {
    this.noContentCode = noContentCode;
    return this;
  }

  @Override
  public void handle(RoutingContext event) {
    HttpServerResponse response = event.response();
    String body = getBody();
    String contentType = getContentType();
    if (contentType != null) response.putHeader(HttpHeaders.CONTENT_TYPE, contentType);
    if (body == null) {
      response.setStatusCode(getNoContentCode());
      response.end();
    } else response.end(body);
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