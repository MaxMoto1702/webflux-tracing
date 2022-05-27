package net.serebryansky.nology.controller

import kotlinx.coroutines.reactive.awaitFirst
import mu.KotlinLogging
import net.serebryansky.common.service.CityService
import net.serebryansky.common.service.PlaceService
import net.serebryansky.common.service.RoutingService
import net.serebryansky.common.service.TripBuilderService
import net.serebryansky.nology.model.Trip
import net.serebryansky.nology.model.TripGenerationRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("trips")
class TripController(
    private val tripBuilderService: TripBuilderService,
    private val cityService: CityService,
    private val placeService: PlaceService,
    private val routingService: RoutingService
) {

    private val log = KotlinLogging.logger {}

    @PostMapping("generate")
    suspend fun generate(@RequestBody request: TripGenerationRequest): ResponseEntity<Trip> {
        log.info { "Generate $request" }
        val trip = Trip()
        val city = cityService.getCity(request.cityId)
        val builderResponse = tripBuilderService.buildTrip(trip)
        val placeIds = builderResponse.placeIds
        val places = placeIds.map { placeService.getPlace(it) }
        val routes = places // send requests to routing
            .windowed(2)
            .filter { it.size == 2 }
            .map { routingService.getRoute(it) }
        trip.city = city
        trip.places = places
        trip.routes = routes
        return ResponseEntity.ok(trip)
    }
}
