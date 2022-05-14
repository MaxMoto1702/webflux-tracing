package net.serebryansky.common.service;

import net.serebryansky.place.model.Place;
import net.serebryansky.routing.model.DurationMatrixResponse;
import net.serebryansky.routing.model.Route;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoutingService {
    Mono<DurationMatrixResponse> getDurationMatrix(List<Place> places);

    Mono<Route> getRoute(List<Place> places);
}
