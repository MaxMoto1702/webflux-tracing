package net.serebryansky.builder.controller

import net.serebryansky.builder.model.TripBuilderResponse
import net.serebryansky.common.service.PlaceService
import net.serebryansky.common.service.RoutingService
import net.serebryansky.common.util.LoggingUtil.Companion.logOnEach
import net.serebryansky.nology.model.Trip
import net.serebryansky.place.model.Place
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.Consumer

@RestController
@RequestMapping
class TripBuilderController(private val placeService: PlaceService, private val routingService: RoutingService) {
    @PostMapping("build")
    fun build(@RequestBody trip: Trip?): ResponseEntity<Mono<TripBuilderResponse>> {
        val response = TripBuilderResponse()
        val places: Flux<Place> = placeService.places
                .cache()
        val durationMatrix = places
                .collectList()
                .flatMap { places: List<Place>? -> routingService.getDurationMatrix(places!!) }
                .cache()
        val placeIds = places
                .map { obj: Place -> obj.id }
        val result = Mono.empty<Any>()
                .doOnEach(logOnEach(Consumer { log.info("Build {}", trip) }))
                .thenMany(places)
                .then(durationMatrix)
                .thenMany(placeIds)
                .collectList()
                .doOnNext { placeIds: List<String?> -> response.placeIds = placeIds }
                .then(Mono.just(response))
        return ResponseEntity.ok(result)
    }

    companion object {
        private val log = LoggerFactory.getLogger(TripBuilderController::class.java)
    }
}
