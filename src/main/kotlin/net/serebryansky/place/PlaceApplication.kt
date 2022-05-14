package net.serebryansky.place

import net.serebryansky.common.filter.RequestIdFilter
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class PlaceApplication {
    @Bean
    fun requestIdFilter(): RequestIdFilter {
        return RequestIdFilter()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(PlaceApplication::class.java, *args)
        }
    }
}
