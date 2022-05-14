package net.serebryansky.nology.controller

import net.serebryansky.builder.model.TripBuilderResponse
import net.serebryansky.city.model.City
import net.serebryansky.common.service.CityService
import net.serebryansky.common.service.PlaceService
import net.serebryansky.common.service.RoutingService
import net.serebryansky.common.service.TripBuilderService
import net.serebryansky.common.util.LoggingUtil.Companion.logOnEach
import net.serebryansky.nology.model.Trip
import net.serebryansky.nology.model.TripGenerationRequest
import net.serebryansky.place.model.Place
import net.serebryansky.routing.model.Route
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
@RequestMapping("trips")
class TripController(private val tripBuilderService: TripBuilderService, private val cityService: CityService, private val placeService: PlaceService, private val routingService: RoutingService) {
    @PostMapping("generate")
    fun generate(@RequestBody request: TripGenerationRequest): ResponseEntity<Mono<Trip>> {
        val trip = Trip()
        val city: Mono<City> = cityService.getCity(request.cityId!!)
                .cache()
        val builderResponse: Mono<TripBuilderResponse> = tripBuilderService.buildTrip(trip)
                .cache()
        val placeIds = builderResponse
                .flux()
                .flatMap { response: TripBuilderResponse -> Flux.fromIterable(response.placeIds) }
        val places = placeIds
                .flatMap { id: String? -> placeService.getPlace(id!!) }
                .cache()
        val routes = places // send requests to routing
                .buffer(2, 1)
                .filter { p: List<Place?> -> p.size == 2 }
                .flatMap { places: List<Place> -> routingService.getRoute(places) }
                .cache()
        val result = Mono.empty<Any>()
                .doOnEach(logOnEach(Consumer { log.info("Generate {}", request) })) // send request to city
                .then(city)
                .doOnNext { city: City? -> trip.city = city } // send request to builder
                .then(builderResponse) // send requests to place service
                .thenMany(places)
                .collectList()
                .doOnNext { places: List<Place?> -> trip.places = places } // send requests to routing
                .thenMany(routes)
                .collectList()
                .doOnNext { routes: List<Route?> -> trip.routes = routes }
                .then(Mono.just(trip))
        return ResponseEntity.ok(result)
    }

    companion object {
        private val log = LoggerFactory.getLogger(TripController::class.java)
    }
}
