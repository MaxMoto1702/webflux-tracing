package net.serebryansky.routing.controller

import mu.KotlinLogging
import net.serebryansky.place.model.Place
import net.serebryansky.routing.model.DurationMatrixResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("durations")
class DurationController {

    private val log = KotlinLogging.logger {}

    @PostMapping("matrix")
    suspend fun matrix(@RequestBody places: List<Place>): ResponseEntity<DurationMatrixResponse> {
        log.info { "Duration matrix for $places" }
        val response = DurationMatrixResponse(
            matrix = places.map { places.map { 10.0 } }
        )
        return ok(response)
    }
}
