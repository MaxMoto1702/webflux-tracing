package net.serebryansky.routing.model

import net.serebryansky.place.model.Place

data class Route (
    val placeStart: Place,
    val placeEnd: Place,
)
