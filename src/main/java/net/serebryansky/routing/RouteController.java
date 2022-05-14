package net.serebryansky.routing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.serebryansky.place.Place;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("routes")
@Slf4j
@RequiredArgsConstructor
public class RouteController {
    @PostMapping("search")
    public ResponseEntity<Mono<Route>> search(@RequestBody RouteSearchRequest request, @RequestHeader(value = "X-Request-ID", required = false) String requestId) {
        log.info("Request #{}", requestId);
        val route = new Route();
        route.setPlaceStart(request.getPlaceStart());
        route.setPlaceEnd(request.getPlaceEnd());
        return ok(Mono.just(route));
    }
}
