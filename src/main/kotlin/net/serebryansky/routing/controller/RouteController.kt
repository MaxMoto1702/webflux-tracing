package net.serebryansky.routing.controller

import mu.KotlinLogging
import net.serebryansky.routing.model.Route
import net.serebryansky.routing.model.RouteSearchRequest
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("routes")
class RouteController {

    private val log = KotlinLogging.logger {}

    @PostMapping("search")
    suspend fun search(@RequestBody request: RouteSearchRequest): ResponseEntity<Route> {
        log.info { "Search route between ${request.placeStart} and ${request.placeEnd}" }
        val route = Route(
            placeStart = request.placeStart,
            placeEnd = request.placeEnd
        )
        return ok(route)
    }
}
