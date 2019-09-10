package ez.spring.vertx.web;

import ez.spring.vertx.EzJob;
import ez.spring.vertx.http.HttpServerConfiguration;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringVertxWebTests {
    @Autowired
    private Vertx vertx;

    private WebClient webClient;

    @Before
    public void start() {
        webClient = WebClient.create(
                vertx, new WebClientOptions().setDefaultHost("localhost").setDefaultPort(HttpServerConfiguration.DEFAULT_PORT)
        );
    }

    @Test
    public void checkTestWebHandler() {
        TestWebHandler.Body body = new TestWebHandler.Body();
        double v = Math.random() * 1000;
        int a = (int) v;
        String b = String.valueOf(v);
        body.setA(a);
        body.setB(b);
        EzJob.create(vertx, "check TestWebHandler")
                .addStep((Object o, Promise<HttpResponse<Buffer>> p) -> webClient.post("/").sendJson(body, p))
                .addStep((HttpResponse<Buffer> res) -> {
                    Assert.assertEquals(
                            "application/json;charset=utf-8",
                            res.getHeader(HttpHeaders.CONTENT_TYPE.toString())
                    );
                    TestWebHandler.Body resBody = res.bodyAsJson(TestWebHandler.Body.class);
                    Assert.assertEquals(resBody.getA(), a);
                    Assert.assertEquals(resBody.getB(), b);
                    return Future.succeededFuture();
                }).startSyncWait()
        ;
    }

    @After
    public void end() {
        webClient.close();
    }
}
