package ez.spring.vertx.web.handler

import ez.spring.vertx.web.handler.request.RequestReader
import ez.spring.vertx.web.handler.response.ResponseWriter
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.impl.HttpStatusException
import org.springframework.lang.Nullable

abstract class WebHandler<Request, Response> : Handler<RoutingContext> {
    var isWithOptionsHandler = true

    override fun handle(routingContext: RoutingContext) {
        val requestReader = requestReader
        val responseWriter = responseWriter
        val request: Request?
        try {
            request = requestReader?.readRequest(routingContext)
        } catch (err: Throwable) {
            routingContext.doFail(KnownError.DECODE_REQUEST_FAILED.withCause(err))
            return
        }
        try {
            exec(request).setHandler {
                if (it.succeeded()) {
                    if (responseWriter == null)
                        routingContext.response().setStatusCode(
                                HttpResponseStatus.NO_CONTENT.code()
                        ).end()
                    else responseWriter.writeResponse(routingContext, it.result())
                } else routingContext.doFail(it.cause())
            }
        } catch (err: Throwable) {
            routingContext.doFail(err)
        }
    }

    @Throws(Throwable::class)
    abstract fun exec(@Nullable request: Request?): Future<Response?>

    @get:Nullable
    abstract val requestReader: RequestReader<Request>?

    @get:Nullable
    abstract val responseWriter: ResponseWriter<Response>?

    fun setWithOptionsHandler(withOptionsHandler: Boolean): WebHandler<Request, Response> {
        isWithOptionsHandler = withOptionsHandler
        return this
    }

    enum class KnownError(private val status: HttpResponseStatus) {
        DECODE_REQUEST_FAILED(HttpResponseStatus.BAD_REQUEST);

        private val ex: HttpStatusException = HttpStatusException(status.code(), name)

        @JvmOverloads
        fun withCause(@Nullable cause: Throwable? = null): HttpStatusException {
            return if (cause == null) ex else HttpStatusException(status.code(), name, cause)
        }
    }
}

fun RoutingContext.doFail(err: Throwable) {
    if (err is HttpStatusException) {
        response().statusMessage = err.payload
        fail(err.statusCode, err)
    } else fail(err)
}