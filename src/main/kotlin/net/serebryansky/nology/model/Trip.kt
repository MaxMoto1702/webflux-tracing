package net.serebryansky.nology.model

import net.serebryansky.city.model.City
import net.serebryansky.place.model.Place
import net.serebryansky.routing.model.Route
import java.util.concurrent.CopyOnWriteArrayList

class Trip {
    var city: City? = null
    var places: List<Place?> = CopyOnWriteArrayList()
    var routes: List<Route?> = CopyOnWriteArrayList()


    override fun equals(o: Any?): Boolean {
        if (o === this) return true
        if (o !is Trip) return false
        val other = o
        if (!other.canEqual(this as Any)) return false
        val `this$city`: Any? = city
        val `other$city`: Any? = other.city
        if (if (`this$city` == null) `other$city` != null else `this$city` != `other$city`) return false
        val `this$places`: Any = places
        val `other$places`: Any = other.places
        if (if (`this$places` == null) `other$places` != null else `this$places` != `other$places`) return false
        val `this$routes`: Any = routes
        val `other$routes`: Any = other.routes
        return if (if (`this$routes` == null) `other$routes` != null else `this$routes` != `other$routes`) false else true
    }

    protected fun canEqual(other: Any?): Boolean {
        return other is Trip
    }

    override fun hashCode(): Int {
        val PRIME = 59
        var result = 1
        val `$city`: Any? = city
        result = result * PRIME + (`$city`?.hashCode() ?: 43)
        val `$places`: Any = places
        result = result * PRIME + (`$places`?.hashCode() ?: 43)
        val `$routes`: Any = routes
        result = result * PRIME + (`$routes`?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String {
        return "Trip(city=" + city + ", places=" + places + ", routes=" + routes + ")"
    }
}
