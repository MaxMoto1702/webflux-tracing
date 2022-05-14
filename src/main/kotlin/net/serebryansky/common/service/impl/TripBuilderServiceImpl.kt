package net.serebryansky.common.service.impl

import net.serebryansky.builder.model.TripBuilderResponse
import net.serebryansky.common.service.TripBuilderService
import net.serebryansky.common.util.logOnNext
import net.serebryansky.nology.model.Trip
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class TripBuilderServiceImpl(private val tripBuilderClient: WebClient, private val applicationName: String) : TripBuilderService {
    override fun buildTrip(trip: Trip): Mono<TripBuilderResponse> {
        return Mono.just(trip)
                .doOnEach(logOnNext<Trip> { log.info("Build trip {}", it) })
                .then(Mono.deferContextual {
                    val requestId = it.get<String>("CONTEXT_KEY")
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
