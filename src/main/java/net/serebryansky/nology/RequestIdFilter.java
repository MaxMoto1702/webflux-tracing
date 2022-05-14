package net.serebryansky.nology;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.util.context.Context;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@Configuration
public class RequestIdFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestId = getRequestId(request.getHeaders());
        return chain
                .filter(exchange)
                .doOnEach(logOnEach(r -> log.info("{} {}", request.getMethod(), request.getURI())))
                .contextWrite(Context.of("CONTEXT_KEY", requestId));
    }

    private String getRequestId(HttpHeaders headers) {
        List<String> requestIdHeaders = headers.get("X-Request-ID");
        return requestIdHeaders == null || requestIdHeaders.isEmpty()
                ? UUID.randomUUID().toString()
                : requestIdHeaders.get(0);
    }

    public static <T> Consumer<Signal<T>> logOnEach(Consumer<T> logStatement) {
        return signal -> {
            String contextValue = signal.getContextView().get("CONTEXT_KEY");
            MDC.put("MDC_KEY", contextValue);
            logStatement.accept(signal.get());
//            try (MDC.MDCCloseable cMdc = MDC.putCloseable("MDC_KEY", contextValue)) {
//                logStatement.accept(signal.get());
//            }
        };
    }

    public static <T> Consumer<Signal<T>> logOnNext(Consumer<T> logStatement) {
        return signal -> {
            if (!signal.isOnNext()) return;
            String contextValue = signal.getContextView().get("CONTEXT_KEY");
            MDC.put("MDC_KEY", contextValue);
            logStatement.accept(signal.get());
//            try (MDC.MDCCloseable cMdc = MDC.putCloseable("MDC_KEY", contextValue)) {
//                logStatement.accept(signal.get());
//            }
        };
    }
}
