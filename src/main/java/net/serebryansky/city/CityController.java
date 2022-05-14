package net.serebryansky.city;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("cities")
@Slf4j
@RequiredArgsConstructor
public class CityController {
    @GetMapping("{id}")
    public ResponseEntity<Mono<City>> get(@PathVariable String id, @RequestHeader(value = "X-Request-ID", required = false) String requestId, @RequestHeader(value = "X-B3-TRACEID", required = false) String traceId) {
        log.info("Request #{}", requestId);
        log.info("Trace #{}", traceId);
        val data = new City();
        data.setId(id);
        data.setName("City #" + id);
        return ok(Mono.just(data));
    }
}
