package net.serebryansky.common.service;

import net.serebryansky.builder.model.TripBuilderResponse;
import net.serebryansky.nology.model.Trip;
import reactor.core.publisher.Mono;

public interface TripBuilderService {
    Mono<TripBuilderResponse> buildTrip(Trip trip);
}
