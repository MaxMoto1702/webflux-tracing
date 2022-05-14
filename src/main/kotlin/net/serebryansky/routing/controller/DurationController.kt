package net.serebryansky.routing.controller

import net.serebryansky.common.util.logOnEach
import net.serebryansky.place.model.Place
import net.serebryansky.routing.model.DurationMatrixResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.function.Consumer
import java.util.stream.Collectors

@RestController
@RequestMapping("durations")
class DurationController {
    @PostMapping("matrix")
    fun matrix(@RequestBody places: List<Place?>): ResponseEntity<Mono<DurationMatrixResponse>> {
        val response = DurationMatrixResponse(
                matrix = places.map { places.map { 10.0 } }
        )
        return ResponseEntity.ok(Mono.empty<Any>()
                .doOnEach(logOnEach(Consumer { log.info("Duration matrix for {}", places) }))
                .then(Mono.just(response)))
    }

    companion object {
        private val log = LoggerFactory.getLogger(DurationController::class.java)
    }
}
