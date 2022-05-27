package net.serebryansky.common.service

import kotlinx.coroutines.flow.Flow
import net.serebryansky.place.model.Place
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PlaceService {
    suspend fun getPlaces(): Flow<Place>
    suspend fun getPlace(id: String): Place
}
