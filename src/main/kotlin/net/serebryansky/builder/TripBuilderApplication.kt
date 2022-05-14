package net.serebryansky.builder

import net.serebryansky.common.filter.RequestIdFilter
import net.serebryansky.common.service.PlaceService
import net.serebryansky.common.service.RoutingService
import net.serebryansky.common.service.impl.PlaceServiceImpl
import net.serebryansky.common.service.impl.RoutingServiceImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class TripBuilderApplication {
    @Bean
    fun placeClient(@Value("\${integrations.placeService.url}") baseUrl: String?): WebClient {
        return WebClient.builder()
                .baseUrl(baseUrl!!)
                .build()
    }

    @Bean
    fun requestIdFilter(): RequestIdFilter {
        return RequestIdFilter()
    }

    @Bean
    fun routingClient(@Value("\${integrations.routingService.url}") baseUrl: String?): WebClient {
        return WebClient.builder()
                .baseUrl(baseUrl!!)
                .build()
    }

    @Bean
    fun placeService(placeClient: WebClient?, applicationName: String?): PlaceService {
        return PlaceServiceImpl(placeClient!!, applicationName!!)
    }

    @Bean
    fun applicationName(): String {
        return "trip-builder"
    }

    @Bean
    fun routingService(routingClient: WebClient?, applicationName: String?): RoutingService {
        return RoutingServiceImpl(routingClient!!, applicationName!!)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(TripBuilderApplication::class.java, *args)
        }
    }
}
