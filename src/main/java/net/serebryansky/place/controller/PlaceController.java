package net.serebryansky.place.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.serebryansky.place.model.Place;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static net.serebryansky.common.util.LoggingUtil.logOnEach;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("places")
@Slf4j
@RequiredArgsConstructor
public class PlaceController {
    @GetMapping
    public ResponseEntity<Flux<Place>> list() {
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
        );
        val places = placeIds.map(id -> {
            val place = new Place();
            place.setId(id);
            place.setName("Place #" + id);
            return place;
        });
        return ok(Mono.empty()
                .doOnEach(logOnEach(() -> log.info("Get place list")))
                .thenMany(places));
    }

    @GetMapping("{id}")
    public ResponseEntity<Mono<Place>> get(@PathVariable String id) {
        val place = new Place();
        place.setId(id);
        place.setName("Place #" + id);
        return ok(Mono.empty()
                .doOnEach(logOnEach(() -> log.info("Get place #{}", id)))
                .then(Mono.just(place)));
    }
}
