package net.serebryansky.routing.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.serebryansky.place.model.Place;
import net.serebryansky.routing.model.DurationMatrixResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static net.serebryansky.common.util.LoggingUtil.logOnEach;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("durations")
@Slf4j
public class DurationController {
    @PostMapping("matrix")
    public ResponseEntity<Mono<DurationMatrixResponse>> matrix(@RequestBody List<Place> places) {
        val response = new DurationMatrixResponse();
        response.setMatrix(places
                .stream()
                .map(placeStart -> places
                        .stream()
                        .map(placeEnd -> 10.0)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList()));
        return ok(Mono.empty()
                .doOnEach(logOnEach(() -> log.info("Duration matrix for {}", places)))
                .then(Mono.just(response)));
    }
}
