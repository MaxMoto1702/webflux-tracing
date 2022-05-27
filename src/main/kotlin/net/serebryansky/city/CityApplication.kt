package net.serebryansky.city

import net.serebryansky.common.OneMethodInterceptor
import net.serebryansky.common.filter.RequestIdFilter
import org.springframework.aop.Advisor
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.EnableAspectJAutoProxy
import java.lang.reflect.Method


@SpringBootApplication
@EnableAspectJAutoProxy
class CityApplication {
    @Bean
    fun requestIdFilter(): RequestIdFilter {
        return RequestIdFilter()
    }

    @Bean
    fun oneMethodInterceptor(): Advisor {
        val a = object : StaticMethodMatcherPointcutAdvisor(OneMethodInterceptor()) {
            override fun matches(method: Method, targetClass: Class<*>): Boolean {
                return targetClass.packageName.startsWith("net.serebryansky.city.controller")
            }
        }
        a.order = 0
        return a
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(CityApplication::class.java, *args)
}
