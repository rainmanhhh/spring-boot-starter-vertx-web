package ez.spring.vertx.web.verticle

import ez.spring.vertx.web.route.EzRouter
import ez.spring.vertx.web.route.RouteProps
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.json.Json
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HttpServerVerticle(private val httpServerOptions: HttpServerOptions) : CoroutineVerticle() {
    private var ezRouter: EzRouter? = null
    private var routes: List<RouteProps> = emptyList()
    private var httpServer: HttpServer? = null

    fun setRoutes(routes: List<RouteProps>): HttpServerVerticle {
        this.routes = routes
        return this
    }

    override suspend fun start() {
        httpServer = vertx.createHttpServer(httpServerOptions)!!.apply {
            ezRouter = EzRouter.router(routes)
            try {
                requestHandler(ezRouter).listenAwait()
                log.info("using httpServer at {}:{}", httpServerOptions.host, actualPort())
            } catch (e: Throwable) {
                log.error("httpServer not available! options: {}", Json.encodePrettily(httpServerOptions))
                throw e
            }
        }
    }

    override suspend fun stop() {
        httpServer?.let {
            log.info("disconnect from httpServer at {}", it.actualPort())
            it.close()
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(HttpServerVerticle::class.java)
    }
}