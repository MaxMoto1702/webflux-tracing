package net.serebryansky.common.service.impl

import net.serebryansky.city.model.City
import net.serebryansky.common.service.CityService
import net.serebryansky.common.util.LoggingUtil
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.context.ContextView
import java.util.function.Consumer

class CityServiceImpl(private val cityClient: WebClient, private val applicationName: String) : CityService {
    override fun getCity(cityId: String): Mono<City> {
        // log.info("Get city {}", cityId);
        return Mono.just(cityId)
                .doOnEach(LoggingUtil.Companion.logOnNext<String>(Consumer { p: String? -> log.info("Get city {}", p) }))
                .then(Mono.deferContextual { contextView: ContextView ->
                    val requestId = contextView.get<String>("CONTEXT_KEY")
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
