package net.serebryansky.common.service

import net.serebryansky.builder.model.TripBuilderResponse
import net.serebryansky.nology.model.Trip
import reactor.core.publisher.Mono

interface TripBuilderService {
    suspend fun buildTrip(trip: Trip): TripBuilderResponse
}
