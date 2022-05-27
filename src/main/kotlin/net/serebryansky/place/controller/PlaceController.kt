package net.serebryansky.place.controller

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import mu.KotlinLogging
import net.serebryansky.place.model.Place
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("places")
class PlaceController {

    private val log = KotlinLogging.logger {}

    @GetMapping
    fun list(): ResponseEntity<Flow<Place>> {
        log.info { "Get place list" }
        val placeIds = flowOf(
            "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9"
        )
        val places = placeIds.map {
            Place(
                id = it,
                name = "Place #$it"
            )
        }
        return ok(places)
    }

    @GetMapping("{id}")
    suspend fun get(@PathVariable id: String): ResponseEntity<Place> {
        log.info { "Get place $id" }
        val place = Place(
            id = id,
            name = "Place #$id"
        )
        return ok(place)
    }
}
