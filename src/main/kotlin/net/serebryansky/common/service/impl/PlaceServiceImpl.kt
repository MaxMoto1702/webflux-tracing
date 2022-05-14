package net.serebryansky.common.service.impl

import net.serebryansky.common.service.PlaceService
import net.serebryansky.common.util.logOnEach
import net.serebryansky.common.util.logOnNext
import net.serebryansky.place.model.Place
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.context.ContextView

class PlaceServiceImpl(private val placeClient: WebClient, private val applicationName: String) : PlaceService {
    override fun getPlaces(): Flux<Place> = Mono.empty<Any>()
            .doOnEach(logOnEach { log.info("Get places") })
            .then(Mono.subscriberContext())
            .flatMapMany {
                val requestId = it.get<String>("CONTEXT_KEY")
                placeClient
                        .get()
                        .uri("/places")
                        .header("X-Request-ID", requestId)
                        .header("X-Source", applicationName)
                        .retrieve()
                        .bodyToFlux(Place::class.java)
            }

    override fun getPlace(id: String): Mono<Place> {
        return Mono.just(id)
                .doOnEach(logOnNext<String> { log.info("Get place {}", it) })
                .then(Mono.deferContextual { contextView: ContextView ->
                    val requestId = contextView.get<String>("CONTEXT_KEY")
                    placeClient
                            .get()
                            .uri("/places/{id}", id)
                            .header("X-Request-ID", requestId)
                            .header("X-Source", applicationName)
                            .retrieve()
                            .bodyToMono(Place::class.java)
                })
    }

    companion object {
        private val log = LoggerFactory.getLogger(PlaceServiceImpl::class.java)
    }
}
