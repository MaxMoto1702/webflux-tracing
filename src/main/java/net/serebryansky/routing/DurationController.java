package net.serebryansky.routing;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.serebryansky.place.Place;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("durations")
@Slf4j
public class DurationController {
    @PostMapping("matrix")
    public ResponseEntity<Mono<DurationMatrixResponse>> matrix(@RequestBody List<Place> places, @RequestHeader(value = "X-Request-ID", required = false) String requestId) {
        log.info("Request #{}", requestId);
        val response = new DurationMatrixResponse();
        response.setMatrix(places
                .stream()
                .map(placeStart -> places
                        .stream()
                        .map(placeEnd -> 10.0)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList()));
        return ok(Mono.just(response));
    }
}
