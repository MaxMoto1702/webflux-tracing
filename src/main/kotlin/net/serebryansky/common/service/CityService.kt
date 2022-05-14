package net.serebryansky.common.service

import net.serebryansky.city.model.City
import reactor.core.publisher.Mono

interface CityService {
    fun getCity(cityId: String): Mono<City>
}
