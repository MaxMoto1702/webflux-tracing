package net.serebryansky.routing.controller

import net.serebryansky.common.util.LoggingUtil.Companion.logOnEach
import net.serebryansky.routing.model.Route
import net.serebryansky.routing.model.RouteSearchRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.function.Consumer

@RestController
@RequestMapping("routes")
class RouteController {
    @PostMapping("search")
    fun search(@RequestBody request: RouteSearchRequest): ResponseEntity<Mono<Route>> {
        val route = Route()
        route.placeStart = request.placeStart
        route.placeEnd = request.placeEnd
        return ResponseEntity.ok(Mono.empty<Any>()
                .doOnEach(logOnEach(Consumer { log.info("Search route between {} and {}", request.placeStart, request.placeEnd) }))
                .then(Mono.just(route)))
    }

    companion object {
        private val log = LoggerFactory.getLogger(RouteController::class.java)
    }
}
