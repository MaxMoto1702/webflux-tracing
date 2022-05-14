package net.serebryansky.nology.model

import net.serebryansky.city.model.City
import net.serebryansky.place.model.Place
import net.serebryansky.routing.model.Route
import java.util.concurrent.CopyOnWriteArrayList

data class Trip(
        var city: City? = null,
        var places: List<Place?> = CopyOnWriteArrayList(),
        var routes: List<Route?> = CopyOnWriteArrayList(),
)
