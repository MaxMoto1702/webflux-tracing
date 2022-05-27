package net.serebryansky.routing

import net.serebryansky.common.OneMethodInterceptor
import net.serebryansky.common.filter.RequestIdFilter
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

    @Bean
    fun oneMethodInterceptor(): Advisor {
        val a = object : StaticMethodMatcherPointcutAdvisor(OneMethodInterceptor()) {
            override fun matches(method: Method, targetClass: Class<*>): Boolean {
                return targetClass.packageName.startsWith("net.serebryansky.routing.controller")
            }
        }
        a.order = 0
        return a
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(RoutingApplication::class.java, *args)
}
