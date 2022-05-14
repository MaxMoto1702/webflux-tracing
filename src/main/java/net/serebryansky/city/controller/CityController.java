package net.serebryansky.city.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.serebryansky.city.model.City;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static net.serebryansky.common.util.LoggingUtil.logOnEach;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("cities")
@Slf4j
@RequiredArgsConstructor
public class CityController {
    @GetMapping("{id}")
    public ResponseEntity<Mono<City>> get(@PathVariable String id) {
        val city = new City();
        city.setId(id);
        city.setName("City #" + id);
        return ok(Mono.empty()
                .doOnEach(logOnEach(() -> log.info("Get city #{}", id)))
                .then(Mono.just(city)));
    }
}
