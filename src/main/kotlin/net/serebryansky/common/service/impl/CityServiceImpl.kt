package net.serebryansky.common.service.impl

import net.serebryansky.city.model.City
import net.serebryansky.common.service.CityService
import net.serebryansky.common.util.logOnNext
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class CityServiceImpl(private val cityClient: WebClient, private val applicationName: String) : CityService {
    override fun getCity(cityId: String): Mono<City> {
        return Mono.just(cityId)
                .doOnEach(logOnNext<String> { log.info("Get city {}", it) })
                .then(Mono.deferContextual {
                    val requestId = it.get<String>("CONTEXT_KEY")
                    cityClient.get()
                            .uri("/cities/{id}", cityId)
                            .header("X-Request-ID", requestId)
                            .header("X-Source", applicationName)
                            .retrieve()
                            .bodyToMono(City::class.java)
                })
    }

    companion object {
        private val log = LoggerFactory.getLogger(CityServiceImpl::class.java)
    }
}
