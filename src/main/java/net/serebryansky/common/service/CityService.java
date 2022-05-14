package net.serebryansky.common.service;

import net.serebryansky.city.model.City;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CityService {
    Mono<City> getCity(String cityId);
}
