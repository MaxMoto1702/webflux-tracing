package net.serebryansky.routing.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.serebryansky.routing.model.Route;
import net.serebryansky.routing.model.RouteSearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static net.serebryansky.common.util.LoggingUtil.logOnEach;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("routes")
@Slf4j
@RequiredArgsConstructor
public class RouteController {
    @PostMapping("search")
    public ResponseEntity<Mono<Route>> search(@RequestBody RouteSearchRequest request) {
        val route = new Route();
        route.setPlaceStart(request.getPlaceStart());
        route.setPlaceEnd(request.getPlaceEnd());
        return ok(Mono.empty()
                .doOnEach(logOnEach(() -> log.info("Search route between {} and {}", request.getPlaceStart(), request.getPlaceEnd())))
                .then(Mono.just(route)));
    }
}
