package net.serebryansky.common.service.impl

import net.serebryansky.common.service.PlaceService
import net.serebryansky.common.util.LoggingUtil
import net.serebryansky.place.model.Place
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.context.Context
import reactor.util.context.ContextView
import java.util.function.Consumer

class PlaceServiceImpl(private val placeClient: WebClient, private val applicationName: String) : PlaceService {
    override val places: Flux<Place>
        get() = Mono.empty<Any>()
                .doOnEach(LoggingUtil.Companion.logOnEach(Runnable { log.info("Get places") }))
                .then(Mono.subscriberContext())
                .flatMapMany { contextView: Context ->
                    val requestId = contextView.get<String>("CONTEXT_KEY")
                    placeClient
                            .get()
                            .uri("/places")
                            .header("X-Request-ID", requestId)
                            .header("X-Source", applicationName)
                            .retrieve()
                            .bodyToFlux(Place::class.java)
                }

    override fun getPlace(placeId: String): Mono<Place> {
        // log.info("Get place {}", placeId);
        return Mono.just(placeId)
                .doOnEach(LoggingUtil.Companion.logOnNext<String>(Consumer { p: String? -> log.info("Get place {}", p) }))
                .then(Mono.deferContextual { contextView: ContextView ->
                    val requestId = contextView.get<String>("CONTEXT_KEY")
                    placeClient
                            .get()
                            .uri("/places/{id}", placeId)
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
