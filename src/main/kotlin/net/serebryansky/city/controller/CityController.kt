package net.serebryansky.city.controller

import net.serebryansky.city.model.City
import net.serebryansky.common.util.logOnEach
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.function.Consumer

@RestController
@RequestMapping("cities")
class CityController {
    @GetMapping("{id}")
    operator fun get(@PathVariable id: String): ResponseEntity<Mono<City>> {
        val city = City(
                id = id,
                name = "City #$id"
        )
        return ResponseEntity.ok(Mono.empty<Any>()
                .doOnEach(logOnEach(Consumer { log.info("Get city #{}", id) }))
                .then(Mono.just(city)))
    }

    companion object {
        private val log = LoggerFactory.getLogger(CityController::class.java)
    }
}
