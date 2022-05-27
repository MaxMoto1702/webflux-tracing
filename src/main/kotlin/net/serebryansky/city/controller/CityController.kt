package net.serebryansky.city.controller

import kotlinx.coroutines.reactive.awaitFirst
import mu.KotlinLogging
import net.serebryansky.city.model.City
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("cities")
class CityController {

    private val log = KotlinLogging.logger {}

    @GetMapping("{id}")
    suspend fun get1(@PathVariable id: String): City {
        log.info { "Get city $id" }
        val city = Mono.just(City(
                id = id,
                name = "City #$id")
        )
        return city.awaitFirst()
    }
}
