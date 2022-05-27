package net.serebryansky.common.service

import net.serebryansky.place.model.Place
import net.serebryansky.routing.model.DurationMatrixResponse
import net.serebryansky.routing.model.Route
import reactor.core.publisher.Mono

interface RoutingService {
    suspend fun getDurationMatrix(places: List<Place>): DurationMatrixResponse
    suspend fun getRoute(places: List<Place>): Route
}
