package ez.spring.vertx.web;

import org.springframework.stereotype.Component;

import ez.spring.vertx.web.handler.WebHandler;
import ez.spring.vertx.web.handler.request.RequestReader;
import ez.spring.vertx.web.handler.response.ResponseWriter;
import io.vertx.core.Future;

@Component
public class TestWebHandler extends WebHandler<TestWebHandler.Data, TestWebHandler.Data> {
  @Override
  public Future<Data> exec(Data data) {
    Data r = new Data();
    r.setValue(data.getValue());
    return Future.succeededFuture(r);
  }

  @Override
  public RequestReader<Data> getRequestReader() {
    return RequestReader.jsonBody(Data.class);
  }

  @Override
  public ResponseWriter<Data> getResponseWriter() {
    return ResponseWriter.json();
  }

  @SuppressWarnings("WeakerAccess")
  static class Data {
    private String value = "";

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }
}
