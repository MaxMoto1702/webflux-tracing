package net.serebryansky.builder.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.serebryansky.builder.model.TripBuilderResponse;
import net.serebryansky.common.service.PlaceService;
import net.serebryansky.common.service.RoutingService;
import net.serebryansky.nology.model.Trip;
import net.serebryansky.place.model.Place;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static net.serebryansky.common.util.LoggingUtil.logOnEach;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class TripBuilderController {

    private final PlaceService placeService;
    private final RoutingService routingService;

    @PostMapping("build")
    public ResponseEntity<Mono<TripBuilderResponse>> build(@RequestBody Trip trip) {
        val response = new TripBuilderResponse();
        val places = placeService.getPlaces()
                .cache();
        val durationMatrix = places
                .collectList()
                .flatMap(routingService::getDurationMatrix)
                .cache();
        val placeIds = places
                .map(Place::getId);
        val result = Mono.empty()
                .doOnEach(logOnEach(() -> log.info("Build {}", trip)))
                .thenMany(places)
                .then(durationMatrix)
                .thenMany(placeIds)
                .collectList()
                .doOnNext(response::setPlaceIds)
                .then(Mono.just(response));
        return ok(result);
    }
}
