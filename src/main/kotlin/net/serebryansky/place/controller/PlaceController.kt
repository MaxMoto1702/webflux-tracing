package net.serebryansky.place.controller

import net.serebryansky.common.util.LoggingUtil.Companion.logOnEach
import net.serebryansky.place.model.Place
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.Consumer

@RestController
@RequestMapping("places")
class PlaceController {
    @GetMapping
    fun list(): ResponseEntity<Flux<Place>> {
        val placeIds = Flux.just(
                "0",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9"
        )
        val places = placeIds.map { id: String ->
            val place = Place()
            place.id = id
            place.name = "Place #$id"
            place
        }
        return ResponseEntity.ok(Mono.empty<Any>()
                .doOnEach(logOnEach(Consumer { log.info("Get place list") }))
                .thenMany(places))
    }

    @GetMapping("{id}")
    operator fun get(@PathVariable id: String): ResponseEntity<Mono<Place>> {
        val place = Place()
        place.id = id
        place.name = "Place #$id"
        return ResponseEntity.ok(Mono.empty<Any>()
                .doOnEach(logOnEach(Consumer { log.info("Get place #{}", id) }))
                .then(Mono.just(place)))
    }

    companion object {
        private val log = LoggerFactory.getLogger(PlaceController::class.java)
    }
}
