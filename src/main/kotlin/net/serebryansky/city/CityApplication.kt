package net.serebryansky.city

import net.serebryansky.common.filter.RequestIdFilter
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class CityApplication {
    @Bean
    fun requestIdFilter(): RequestIdFilter {
        return RequestIdFilter()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(CityApplication::class.java, *args)
}
