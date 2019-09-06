package ez.spring.vertx.web;

import ez.spring.vertx.web.handler.WebHandler;
import ez.spring.vertx.web.handler.request.RequestReader;
import ez.spring.vertx.web.handler.response.JsonResponseWriter;
import ez.spring.vertx.web.handler.response.ResponseWriter;
import io.vertx.core.Future;

public class TestWebHandler extends WebHandler<TestWebHandler.Body, TestWebHandler.Body> {
    @Override
    public Future<Body> exec(Body o) throws Throwable {
        return Future.succeededFuture(o);
    }

    @Override
    public RequestReader<Body> getRequestReader() {
        return RequestReader.jsonBody(Body.class);
    }

    @Override
    public JsonResponseWriter<Body> getResponseWriter() {
        return ResponseWriter.json();
    }

    static class Body {
        private int a;
        private String b;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }
    }
}
