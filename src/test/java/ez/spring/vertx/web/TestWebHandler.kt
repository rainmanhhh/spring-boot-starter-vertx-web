package ez.spring.vertx.web

import ez.spring.vertx.web.handler.AsyncWebHandler
import ez.spring.vertx.web.handler.request.RequestReader
import ez.spring.vertx.web.handler.response.JsonResponseWriter
import ez.spring.vertx.web.handler.response.ResponseWriter
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import java.util.concurrent.ThreadLocalRandom

class TestWebHandler : AsyncWebHandler<TestWebHandler.Body, TestWebHandler.Body>() {
    private val log = LoggerFactory.getLogger(javaClass)
    override suspend fun execAsync(request: Body?): Body? {
        log.info("res1 {}", request?.b)
        delay(200 + (ThreadLocalRandom.current().nextDouble() * 400).toLong())
        log.info("res2 {}", request?.b)
        return request
    }

    override val requestReader: RequestReader<Body>?
        get() = RequestReader.jsonBody(Body::class.java)

    override val responseWriter: JsonResponseWriter<Body>?
        get() = ResponseWriter.json()

    class Body {
        var a = 0
        var b: String? = null
    }
}