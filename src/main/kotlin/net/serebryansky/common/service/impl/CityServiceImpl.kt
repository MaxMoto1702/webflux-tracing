package net.serebryansky.common.service.impl

import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import net.serebryansky.city.model.City
import net.serebryansky.common.requestKey
import net.serebryansky.common.service.CityService
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import reactor.core.publisher.Mono.*

class CityServiceImpl(private val cityClient: WebClient, private val applicationName: String) : CityService {

    private val log = KotlinLogging.logger {}

    override suspend fun getCity(cityId: String): City {
        log.info { "Get city $cityId" }
        val requestId = deferContextual { mono { it.get<String>(requestKey) } }.awaitFirst()
        return cityClient.get()
            .uri("/cities/{id}", cityId)
            .header("X-Request-ID", requestId)
            .header("X-Source", applicationName)
            .retrieve()
            .awaitBody()
    }
}
