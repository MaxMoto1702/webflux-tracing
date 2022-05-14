package net.serebryansky.common.filter

import net.serebryansky.common.util.logOnNext
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.util.context.Context
import java.util.*

class RequestIdFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        val requestId = getRequestId(request.headers)
        val invoker = getInvoker(request.headers)
        return chain
                .filter(exchange)
                .doOnEach(logOnNext { log.info("{} {}", request.method, request.uri) })
                .contextWrite(Context.of("CONTEXT_KEY", requestId, "INVOKER", invoker))
    }

    private fun getInvoker(headers: HttpHeaders): String {
        val requestIdHeaders = headers["X-Source"]
        return if (requestIdHeaders == null || requestIdHeaders.isEmpty()) "UNKNOWN" else requestIdHeaders[0]
    }

    private fun getRequestId(headers: HttpHeaders): String {
        val requestIdHeaders = headers["X-Request-ID"]
        return if (requestIdHeaders == null || requestIdHeaders.isEmpty()) UUID.randomUUID().toString() else requestIdHeaders[0]
    }

    companion object {
        private val log = LoggerFactory.getLogger(RequestIdFilter::class.java)
    }
}
