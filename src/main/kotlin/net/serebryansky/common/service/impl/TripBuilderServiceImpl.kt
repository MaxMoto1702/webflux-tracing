package net.serebryansky.common.service.impl

import net.serebryansky.builder.model.TripBuilderResponse
import net.serebryansky.common.service.TripBuilderService
import net.serebryansky.common.util.LoggingUtil
import net.serebryansky.nology.model.Trip
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.context.ContextView
import java.util.function.Consumer

class TripBuilderServiceImpl(private val tripBuilderClient: WebClient, private val applicationName: String) : TripBuilderService {
    override fun buildTrip(trip: Trip): Mono<TripBuilderResponse> {
        // log.info("Build trip {}", trip);
        return Mono.just(trip)
                .doOnEach(LoggingUtil.Companion.logOnNext<Trip>(Consumer { p: Trip? -> log.info("Build trip {}", p) }))
                .then(Mono.deferContextual { contextView: ContextView ->
                    val requestId = contextView.get<String>("CONTEXT_KEY")
                    tripBuilderClient
                            .post()
                            .uri("build")
                            .header("X-Request-ID", requestId)
                            .header("X-Source", applicationName)
                            .bodyValue(trip)
                            .retrieve()
                            .bodyToMono(TripBuilderResponse::class.java)
                })
    }

    companion object {
        private val log = LoggerFactory.getLogger(TripBuilderServiceImpl::class.java)
    }
}
