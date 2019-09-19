package ez.spring.vertx.web.handler

import ez.spring.vertx.util.EzUtil
import ez.spring.vertx.util.toPromise
import io.vertx.core.Future
import kotlinx.coroutines.async

abstract class AsyncWebHandler<Request, Response> : WebHandler<Request, Response>() {
    override fun exec(request: Request?): Future<Response?> {
        return EzUtil.mdcScope().async {
            execAsync(request)
        }.toPromise().future()
    }

    abstract suspend fun execAsync(request: Request?): Response?
}