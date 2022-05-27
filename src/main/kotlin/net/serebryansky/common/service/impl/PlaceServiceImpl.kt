package net.serebryansky.common.service.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import net.serebryansky.common.service.PlaceService
import net.serebryansky.place.model.Place
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToFlow
import reactor.core.publisher.Mono

class PlaceServiceImpl(private val placeClient: WebClient, private val applicationName: String) : PlaceService {

    private val log = KotlinLogging.logger {}

    override suspend fun getPlaces(): Flow<Place> {
        log.info { "Get places" }
        val requestId = Mono.deferContextual { mono { it.get<String>("CONTEXT_KEY") } }.awaitFirst()
        return placeClient
            .get()
            .uri("/places")
            .header("X-Request-ID", requestId)
            .header("X-Source", applicationName)
            .retrieve()
            .bodyToFlow()
    }

    override suspend fun getPlace(id: String): Place {
        log.info { "Get place $id" }
        val requestId = Mono.deferContextual { mono { it.get<String>("CONTEXT_KEY") } }.awaitFirst()
        return placeClient
            .get()
            .uri("/places/{id}", id)
            .header("X-Request-ID", requestId)
            .header("X-Source", applicationName)
            .retrieve()
            .awaitBody()
    }
}
