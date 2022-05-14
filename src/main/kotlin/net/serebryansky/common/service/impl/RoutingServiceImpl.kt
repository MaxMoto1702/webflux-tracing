package net.serebryansky.common.service.impl

import net.serebryansky.common.service.RoutingService
import net.serebryansky.common.util.logOnNext
import net.serebryansky.place.model.Place
import net.serebryansky.routing.model.DurationMatrixResponse
import net.serebryansky.routing.model.Route
import net.serebryansky.routing.model.RouteSearchRequest
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class RoutingServiceImpl(private val routingClient: WebClient, private val applicationName: String) : RoutingService {
    override fun getDurationMatrix(places: List<Place>): Mono<DurationMatrixResponse> {
        return Mono.just(places)
                .doOnEach(logOnNext<List<Place>> { log.info("Get duration matrix for {}", it) })
                .then(Mono.deferContextual {
                    val requestId = it.get<String>("CONTEXT_KEY")
                    routingClient.post()
                            .uri("/durations/matrix")
                            .header("X-Request-ID", requestId)
                            .header("X-Source", applicationName)
                            .bodyValue(places)
                            .retrieve()
                            .bodyToMono(DurationMatrixResponse::class.java)
                })
    }

    override fun getRoute(places: List<Place>): Mono<Route> {
        return Mono.just(places)
                .doOnEach(logOnNext<List<Place>> { log.info("Get route between {} and {}", it[0], it[1]) })
                .then(Mono.deferContextual {
                    val requestId = it.get<String>("CONTEXT_KEY")
                    routingClient
                            .post()
                            .uri("/routes/search")
                            .header("X-Request-ID", requestId)
                            .header("X-Source", applicationName)
                            .bodyValue(RouteSearchRequest(places[0], places[1]))
                            .retrieve()
                            .bodyToMono(Route::class.java)
                })
    }

    companion object {
        private val log = LoggerFactory.getLogger(RoutingServiceImpl::class.java)
    }
}
