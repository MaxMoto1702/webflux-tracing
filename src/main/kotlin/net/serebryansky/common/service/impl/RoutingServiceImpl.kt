package net.serebryansky.common.service.impl

import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import net.serebryansky.common.requestKey
import net.serebryansky.common.service.RoutingService
import net.serebryansky.place.model.Place
import net.serebryansky.routing.model.DurationMatrixResponse
import net.serebryansky.routing.model.Route
import net.serebryansky.routing.model.RouteSearchRequest
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import reactor.core.publisher.Mono

class RoutingServiceImpl(
    private val routingClient: WebClient,
    private val applicationName: String,
) : RoutingService {

    private val log = KotlinLogging.logger {}

    override suspend fun getDurationMatrix(places: List<Place>): DurationMatrixResponse {
        log.info { "Get duration matrix for $places" }
        val requestId = Mono.deferContextual { mono { it.get<String>(requestKey) } }.awaitFirst()
        return routingClient
            .post()
            .uri("/durations/matrix")
            .header("X-Request-ID", requestId)
            .header("X-Source", applicationName)
            .bodyValue(places)
            .retrieve()
            .awaitBody()
    }

    override suspend fun getRoute(places: List<Place>): Route {
        log.info { "Get route between ${places[0]} and ${places[1]}" }
        val requestId = Mono.deferContextual { mono { it.get<String>(requestKey) } }.awaitFirst()
        return routingClient
            .post()
            .uri("/routes/search")
            .header("X-Request-ID", requestId)
            .header("X-Source", applicationName)
            .bodyValue(RouteSearchRequest(places[0], places[1]))
            .retrieve()
            .awaitBody()
    }
}
