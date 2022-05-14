package net.serebryansky.common.service;

import net.serebryansky.place.model.Place;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlaceService {
    Flux<Place> getPlaces();

    Mono<Place> getPlace(String id);
}
