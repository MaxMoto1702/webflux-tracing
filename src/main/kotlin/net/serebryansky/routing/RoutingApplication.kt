package net.serebryansky.routing

import net.serebryansky.common.filter.RequestIdFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class RoutingApplication {
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

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(RoutingApplication::class.java, *args)
        }
    }
}
