package ez.spring.vertx.web

import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.dispatcher
import kotlin.coroutines.CoroutineContext

object EzWebUtil {
    const val COROUTINE_CONTEXT = "coroutineContext"
}

val RoutingContext.coroutineContext: CoroutineContext
    get() {
        var v = get<CoroutineContext>(EzWebUtil.COROUTINE_CONTEXT)
        if (v == null) v = vertx().dispatcher()
        return v
    }