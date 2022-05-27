package net.serebryansky.nology

import net.serebryansky.common.OneMethodInterceptor
import net.serebryansky.common.filter.RequestIdFilter
import net.serebryansky.common.service.CityService
import net.serebryansky.common.service.PlaceService
import net.serebryansky.common.service.RoutingService
import net.serebryansky.common.service.TripBuilderService
import net.serebryansky.common.service.impl.CityServiceImpl
import net.serebryansky.common.service.impl.PlaceServiceImpl
import net.serebryansky.common.service.impl.RoutingServiceImpl
import net.serebryansky.common.service.impl.TripBuilderServiceImpl
import org.springframework.aop.Advisor
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.web.reactive.function.client.WebClient
import java.lang.reflect.Method

@SpringBootApplication
@EnableAspectJAutoProxy
class NologyApplication {
    @Bean
    fun tripBuilderClient(@Value("\${integrations.tripBuilderService.url}") baseUrl: String?): WebClient {
        return WebClient.builder()
                .baseUrl(baseUrl!!)
                .build()
    }

    @Bean
    fun oneMethodInterceptor(): Advisor {
        val a = object : StaticMethodMatcherPointcutAdvisor(OneMethodInterceptor()) {
            override fun matches(method: Method, targetClass: Class<*>): Boolean {
                return targetClass.packageName.startsWith("net.serebryansky.nology.controller")
            }
        }
        a.order = 0
        return a
    }

    @Bean
    fun cityClient(@Value("\${integrations.cityService.url}") baseUrl: String?): WebClient {
        return WebClient.builder()
                .baseUrl(baseUrl!!)
                .build()
    }

    @Bean
    fun placeClient(@Value("\${integrations.placeService.url}") baseUrl: String?): WebClient {
        return WebClient.builder()
                .baseUrl(baseUrl!!)
                .build()
    }

    @Bean
    fun routingClient(@Value("\${integrations.routingService.url}") baseUrl: String?): WebClient {
        return WebClient.builder()
                .baseUrl(baseUrl!!)
                .build()
    }

    @Bean
    fun requestIdFilter(): RequestIdFilter {
        return RequestIdFilter()
    }

    @Bean
    fun placeService(placeClient: WebClient?, applicationName: String?): PlaceService {
        return PlaceServiceImpl(placeClient!!, applicationName!!)
    }

    @Bean
    fun applicationName(): String {
        return "nology"
    }

    @Bean
    fun routingService(routingClient: WebClient?, applicationName: String?): RoutingService {
        return RoutingServiceImpl(routingClient!!, applicationName!!)
    }

    @Bean
    fun tripBuilderService(tripBuilderClient: WebClient?, applicationName: String?): TripBuilderService {
        return TripBuilderServiceImpl(tripBuilderClient!!, applicationName!!)
    }

    @Bean
    fun cityService(cityClient: WebClient?, applicationName: String?): CityService {
        return CityServiceImpl(cityClient!!, applicationName!!)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(NologyApplication::class.java, *args)
}
