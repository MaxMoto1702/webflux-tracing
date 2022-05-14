package net.serebryansky.common.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serebryansky.common.service.PlaceService;
import net.serebryansky.place.model.Place;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static net.serebryansky.common.util.LoggingUtil.logOnEach;
import static net.serebryansky.common.util.LoggingUtil.logOnNext;

@Slf4j
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final WebClient placeClient;

    @Override
    public Flux<Place> getPlaces() {
        return Mono.empty()
                .doOnEach(logOnEach(() -> log.info("Get places")))
                .then(Mono.subscriberContext())
                .flatMapMany(contextView -> {
                    String requestId = contextView.get("CONTEXT_KEY");
                    String invoker = contextView.get("INVOKER");
                    return placeClient
                            .get()
                            .uri("/places")
                            .header("X-Request-ID", requestId)
                            .header("X-Source", invoker)
                            .retrieve()
                            .bodyToFlux(Place.class);
                });
    }

    @Override
    public Mono<Place> getPlace(String placeId) {
        // log.info("Get place {}", placeId);
        return Mono.just(placeId)
                .doOnEach(logOnNext(p -> log.info("Get place {}", p)))
                .then(Mono.deferContextual(contextView -> {
                    String requestId = contextView.get("CONTEXT_KEY");
                    String invoker = contextView.get("INVOKER");
                    return placeClient
                            .get()
                            .uri("/places/{id}", placeId)
                            .header("X-Request-ID", requestId)
                            .header("X-Source", invoker)
                            .retrieve()
                            .bodyToMono(Place.class);
                }));
    }
}
