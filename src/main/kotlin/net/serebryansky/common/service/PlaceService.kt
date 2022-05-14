package net.serebryansky.common.service

import net.serebryansky.place.model.Place
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PlaceService {
    val places: Flux<Place>
    fun getPlace(id: String): Mono<Place>
}
