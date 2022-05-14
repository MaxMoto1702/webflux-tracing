package net.serebryansky.nology.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.serebryansky.common.service.PlaceService;
import net.serebryansky.common.service.RoutingService;
import net.serebryansky.nology.model.TripGenerationRequest;
import net.serebryansky.nology.model.Trip;
import net.serebryansky.common.service.CityService;
import net.serebryansky.common.service.TripBuilderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static net.serebryansky.common.util.LoggingUtil.logOnEach;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("trips")
@Slf4j
@RequiredArgsConstructor
public class TripController {

    private final TripBuilderService tripBuilderService;
    private final CityService cityService;
    private final PlaceService placeService;
    private final RoutingService routingService;

    @PostMapping("generate")
    public ResponseEntity<Mono<Trip>> generate(@RequestBody TripGenerationRequest request) {
        val trip = new Trip();
        val city = cityService.getCity(request.getCityId())
                .cache();
        val builderResponse = tripBuilderService.buildTrip(trip)
                .cache();
        Flux<String> placeIds = builderResponse
                .flux()
                .flatMap(response -> Flux.fromIterable(response.getPlaceIds()));
        val places = placeIds
                .flatMap(placeService::getPlace)
                .cache();
        val routes = places
                // send requests to routing
                .buffer(2, 1)
                .filter(p -> p.size() == 2)
                .flatMap(routingService::getRoute)
                .cache();
        val result = Mono.empty()
                .doOnEach(logOnEach(() -> log.info("Generate {}", request)))
                // send request to city
                .then(city)
                .doOnNext(trip::setCity)
                // send request to builder
                .then(builderResponse)
                // send requests to place service
                .thenMany(places)
                .collectList()
                .doOnNext(trip::setPlaces)
                // send requests to routing
                .thenMany(routes)
                .collectList()
                .doOnNext(trip::setRoutes)
                .then(Mono.just(trip));
        return ok(result);
    }








}
