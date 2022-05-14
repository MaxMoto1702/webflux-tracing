package net.serebryansky.nology;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.serebryansky.builder.TripBuilderResponse;
import net.serebryansky.city.City;
import net.serebryansky.place.Place;
import net.serebryansky.routing.Route;
import net.serebryansky.routing.RouteSearchRequest;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static net.serebryansky.nology.LoggingUtil.logOnEach;
import static net.serebryansky.nology.LoggingUtil.logOnNext;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("trips")
@Slf4j
@RequiredArgsConstructor
public class TripController {

    private final WebClient tripBuilderClient;
    private final WebClient cityClient;
    private final WebClient placeClient;
    private final WebClient routingClient;

    @PostMapping("generate")
    public ResponseEntity<Mono<Trip>> generate(@RequestBody TripGenerationRequest request, @RequestHeader(value = "X-Request-ID", required = false) String requestId) {
        log.info("Request #{}", requestId);
        val trip = new Trip();
        val city = getCity(request.getCityId());
        val builderResponse = buildTrip(trip);
        Flux<String> placeIds = builderResponse
                .flux()
                .flatMap(response -> Flux.fromIterable(response.getPlaceIds()));
        val places = placeIds
                .flatMap(this::getPlace);
        val routes = places
                // send requests to routing
                .buffer(2, 1)
                .filter(p -> p.size() == 2)
                .flatMap(this::getRoute);
        val result = Mono.empty()
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

    private Mono<Route> getRoute(List<Place> placePair) {
        // log.info("Get route between {} and {}", placePair.get(0), placePair.get(1));
        return Mono.just(placePair)
                .doOnEach(logOnNext(p -> log.info("Get route between {} and {}", p.get(0), p.get(1))))
                .then(Mono.deferContextual(contextView -> {
                    String requestId = contextView.get("CONTEXT_KEY");
                    return routingClient
                            .post()
                            .uri("/routes/search")
                            .header("X-Request-ID", requestId)
                            .bodyValue(new RouteSearchRequest(placePair.get(0), placePair.get(1)))
                            .retrieve()
                            .bodyToMono(Route.class)
                            .cache();
                }));
    }

    private Mono<Place> getPlace(String placeId) {
        // log.info("Get place {}", placeId);
        return Mono.just(placeId)
                .doOnEach(logOnNext(p -> log.info("Get place {}", p)))
                .then(Mono.deferContextual(contextView -> {
                    String requestId = contextView.get("CONTEXT_KEY");
                    return placeClient
                            .get()
                            .uri("/places/{id}", placeId)
                            .header("X-Request-ID", requestId)
                            .retrieve()
                            .bodyToMono(Place.class)
                            .cache();
                }));
    }

    private Mono<TripBuilderResponse> buildTrip(Trip trip) {
        // log.info("Build trip {}", trip);
        return Mono.just(trip)
                .doOnEach(logOnNext(p -> log.info("Build trip {}", p)))
                .then(Mono.deferContextual(contextView -> {
                            String requestId = contextView.get("CONTEXT_KEY");
                            return tripBuilderClient
                                    .post()
                                    .uri("build")
                                    .header("X-Request-ID", requestId)
                                    .header("X-B3-TRACEID", MDC.get("X-B3-TraceId"))
                                    .bodyValue(trip)
                                    .retrieve()
                                    .bodyToMono(TripBuilderResponse.class)
                                    .cache();
                        }
                ));
    }

    private Mono<City> getCity(String cityId) {
        // log.info("Get city {}", cityId);
        return Mono.just(cityId)
                .doOnEach(logOnNext(p -> log.info("Get city {}", p)))
                .then(Mono.deferContextual(contextView -> {
                            String requestId = contextView.get("CONTEXT_KEY");
                            return cityClient.get()
                                    .uri("/cities/{id}", cityId)
                                    .header("X-Request-ID", requestId)
                                    .header("X-B3-TRACEID", MDC.get("X-B3-TraceId"))
                                    .retrieve()
                                    .bodyToMono(City.class)
                                    .cache();
                        }
                ));
    }
}
