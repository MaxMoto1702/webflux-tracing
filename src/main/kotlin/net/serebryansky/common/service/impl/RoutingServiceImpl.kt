package net.serebryansky.common.service.impl

import net.serebryansky.common.service.RoutingService
import net.serebryansky.common.util.LoggingUtil
import net.serebryansky.place.model.Place
import net.serebryansky.routing.model.DurationMatrixResponse
import net.serebryansky.routing.model.Route
import net.serebryansky.routing.model.RouteSearchRequest
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.context.ContextView
import java.util.function.Consumer

class RoutingServiceImpl(private val routingClient: WebClient, private val applicationName: String) : RoutingService {
    override fun getDurationMatrix(p: List<Place>): Mono<DurationMatrixResponse?> {
        return Mono.just(p)
                .doOnEach(LoggingUtil.Companion.logOnNext<List<Place>>(Consumer { p1: List<Place?>? -> log.info("Get duration matrix for {}", p1) }))
                .then(Mono.deferContextual { contextView: ContextView ->
                    val requestId = contextView.get<String>("CONTEXT_KEY")
                    routingClient.post()
                            .uri("/durations/matrix")
                            .header("X-Request-ID", requestId)
                            .header("X-Source", applicationName)
                            .bodyValue(p)
                            .retrieve()
                            .bodyToMono(DurationMatrixResponse::class.java)
                })
    }

    override fun getRoute(placePair: List<Place>): Mono<Route?> {
        // log.info("Get route between {} and {}", placePair.get(0), placePair.get(1));
        return Mono.just(placePair)
                .doOnEach(LoggingUtil.Companion.logOnNext<List<Place>>(Consumer { p: List<Place> -> log.info("Get route between {} and {}", p[0], p[1]) }))
                .then(Mono.deferContextual { contextView: ContextView ->
                    val requestId = contextView.get<String>("CONTEXT_KEY")
                    routingClient
                            .post()
                            .uri("/routes/search")
                            .header("X-Request-ID", requestId)
                            .header("X-Source", applicationName)
                            .bodyValue(RouteSearchRequest(placePair[0], placePair[1]))
                            .retrieve()
                            .bodyToMono(Route::class.java)
                })
    }

    companion object {
        private val log = LoggerFactory.getLogger(RoutingServiceImpl::class.java)
    }
}
