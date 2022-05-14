package net.serebryansky.common.service

import net.serebryansky.place.model.Place
import net.serebryansky.routing.model.DurationMatrixResponse
import net.serebryansky.routing.model.Route
import reactor.core.publisher.Mono

interface RoutingService {
    fun getDurationMatrix(places: List<Place>): Mono<DurationMatrixResponse>
    fun getRoute(places: List<Place>): Mono<Route>
}
