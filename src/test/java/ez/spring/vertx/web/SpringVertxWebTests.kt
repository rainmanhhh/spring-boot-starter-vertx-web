package ez.spring.vertx.web

import ez.spring.vertx.http.HttpServerConfiguration
import ez.spring.vertx.web.TestWebHandler.Body
import io.vertx.core.Vertx
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.kotlin.ext.web.client.sendJsonAwait
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class SpringVertxWebTests {
    private val log = LoggerFactory.getLogger(javaClass)
    private val vertx: Vertx = Vertx.vertx()
    private val webClient: WebClient = WebClient.create(
            vertx, WebClientOptions().setDefaultHost("localhost").setDefaultPort(HttpServerConfiguration.DEFAULT_PORT)
    )

    @Test
    fun checkTestWebHandler() {
        val list = mutableListOf<Job>()
        repeat(3) {
            list += GlobalScope.launch {
                val body = Body()
                val v = Math.random() * 1000
                val a = v.toInt()
                val b = v.toString()
                body.a = a
                body.b = b
                val res = webClient.post("/").sendJsonAwait(body)
                Assert.assertEquals(
                        "application/json;charset=utf-8",
                        res.getHeader(HttpHeaders.CONTENT_TYPE.toString())
                )
                val resBody: Body = res.bodyAsJson(Body::class.java)
                Assert.assertEquals(resBody.a.toLong(), a.toLong())
                Assert.assertEquals(resBody.b, b)
            }
        }
        runBlocking {
            list.joinAll()
        }
    }

    @After
    fun end() {
        webClient.close()
    }
}