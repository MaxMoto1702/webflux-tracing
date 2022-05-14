package net.serebryansky.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;
import java.util.UUID;

import static net.serebryansky.common.util.LoggingUtil.logOnEach;

@Slf4j
public class RequestIdFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestId = getRequestId(request.getHeaders());
        String invoker = getInvoker(request.getHeaders());
        return chain
                .filter(exchange)
                .doOnEach(logOnEach(r -> log.info("{} {}", request.getMethod(), request.getURI())))
                .contextWrite(Context.of("CONTEXT_KEY", requestId, "INVOKER", invoker));
    }

    private String getInvoker(HttpHeaders headers) {
        List<String> requestIdHeaders = headers.get("X-Source");
        return requestIdHeaders == null || requestIdHeaders.isEmpty()
                ? "UNKNOWN"
                : requestIdHeaders.get(0);
    }

    private String getRequestId(HttpHeaders headers) {
        List<String> requestIdHeaders = headers.get("X-Request-ID");
        return requestIdHeaders == null || requestIdHeaders.isEmpty()
                ? UUID.randomUUID().toString()
                : requestIdHeaders.get(0);
    }
}
