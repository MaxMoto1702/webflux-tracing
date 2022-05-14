package net.serebryansky.builder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.serebryansky.nology.Trip;
import net.serebryansky.place.Place;
import net.serebryansky.routing.DurationMatrixResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class TripBuilderController {
    private final WebClient placeClient;
    private final WebClient routingClient;

    @PostMapping("build")
    public ResponseEntity<Mono<TripBuilderResponse>> build(@RequestBody Trip ignoredTrip, @RequestHeader(value = "X-Request-ID", required = false) String requestId) {
        log.info("Request #{}", requestId);
        val response = new TripBuilderResponse();
        val places = placeClient
                .get()
                .uri("/places")
                .retrieve()
                .bodyToFlux(Place.class);
        val durationMatrix = places
                .collectList()
                .flatMap(p -> routingClient.post()
                        .uri("/durations/matrix")
                        .bodyValue(p)
                        .retrieve()
                        .bodyToMono(DurationMatrixResponse.class));
        val placeIds = places
                .map(Place::getId)
                .doOnNext(id -> response.getPlaceIds().add(id));
        val result = places
                .then(durationMatrix)
                .thenMany(placeIds)
                .then(Mono.just(response));
        return ok(result);
    }
}
