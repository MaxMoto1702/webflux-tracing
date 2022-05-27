package net.serebryansky.builder.controller

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import mu.KotlinLogging
import net.serebryansky.builder.model.TripBuilderResponse
import net.serebryansky.common.service.PlaceService
import net.serebryansky.common.service.RoutingService
import net.serebryansky.nology.model.Trip
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class TripBuilderController(
    private val placeService: PlaceService,
    private val routingService: RoutingService
) {

    private val log = KotlinLogging.logger {}

    @PostMapping("build")
    suspend fun build(@RequestBody trip: Trip): ResponseEntity<TripBuilderResponse> {
        log.info { "Build $trip" }
        val places = placeService.getPlaces()
        val durationMatrix = routingService.getDurationMatrix(places.toList())
        val placeIds = places.map { it.id }.toList()
        val response = TripBuilderResponse(placeIds = placeIds)
        return ResponseEntity.ok(response)
    }
}
