package ez.spring.vertx.web;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Timed;
import org.springframework.test.context.junit4.SpringRunner;

import ez.spring.vertx.EzJob;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringVertxWebTests {
  private Logger log = LoggerFactory.getLogger(getClass());
  @Autowired
  private HttpServerOptions httpServerOptions;
  private Vertx vertx;
  private WebClient webClient;

  @Before
  public void start() {
    vertx = Vertx.vertx();
    webClient = WebClient.create(vertx, new WebClientOptions()
      .setDefaultHost("localhost")
      .setDefaultPort(httpServerOptions.getPort())
    );
  }

  @Timed(millis = 2000)
  @Test
  public void checkTestWebHandler() {
    TestWebHandler.Data req = new TestWebHandler.Data();
    req.setValue(String.valueOf(Math.random()));
    TestWebHandler.Data resp = EzJob.create(vertx, "send request to TestWebHandler").<HttpResponse<Buffer>>then(
      p -> webClient.post("/").sendJson(req, p)
    ).thenCompose(r -> Future.succeededFuture(
      Json.decodeValue(r.body(), TestWebHandler.Data.class)
    )).join();
    Assert.assertEquals(req.getValue(), resp.getValue());
  }

  @After
  public void end() {
    webClient.close();
    vertx.close();
  }
}
