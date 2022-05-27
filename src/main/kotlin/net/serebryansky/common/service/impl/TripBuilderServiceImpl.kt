package net.serebryansky.common.service.impl

import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import net.serebryansky.builder.model.TripBuilderResponse
import net.serebryansky.common.service.TripBuilderService
import net.serebryansky.nology.model.Trip
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import reactor.core.publisher.Mono

class TripBuilderServiceImpl(
    private val tripBuilderClient: WebClient,
    private val applicationName: String,
) : TripBuilderService {

    private val log = KotlinLogging.logger {}

    override suspend fun buildTrip(trip: Trip): TripBuilderResponse {
        log.info { "Build trip $trip" }
        val requestId = Mono.deferContextual { mono { it.get<String>("CONTEXT_KEY") } }.awaitFirst()
        return tripBuilderClient
            .post()
            .uri("build")
            .header("X-Request-ID", requestId)
            .header("X-Source", applicationName)
            .bodyValue(trip)
            .retrieve()
            .awaitBody()
    }
}
