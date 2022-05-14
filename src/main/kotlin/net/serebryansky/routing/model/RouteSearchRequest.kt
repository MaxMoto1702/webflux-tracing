package net.serebryansky.routing.model

import net.serebryansky.place.model.Place

class RouteSearchRequest {
    var placeStart: Place? = null
    var placeEnd: Place? = null

    constructor(placeStart: Place?, placeEnd: Place?) {
        this.placeStart = placeStart
        this.placeEnd = placeEnd
    }

    constructor() {}

    override fun equals(o: Any?): Boolean {
        if (o === this) return true
        if (o !is RouteSearchRequest) return false
        val other = o
        if (!other.canEqual(this as Any)) return false
        val `this$placeStart`: Any? = placeStart
        val `other$placeStart`: Any? = other.placeStart
        if (if (`this$placeStart` == null) `other$placeStart` != null else `this$placeStart` != `other$placeStart`) return false
        val `this$placeEnd`: Any? = placeEnd
        val `other$placeEnd`: Any? = other.placeEnd
        return if (if (`this$placeEnd` == null) `other$placeEnd` != null else `this$placeEnd` != `other$placeEnd`) false else true
    }

    protected fun canEqual(other: Any?): Boolean {
        return other is RouteSearchRequest
    }

    override fun hashCode(): Int {
        val PRIME = 59
        var result = 1
        val `$placeStart`: Any? = placeStart
        result = result * PRIME + (`$placeStart`?.hashCode() ?: 43)
        val `$placeEnd`: Any? = placeEnd
        result = result * PRIME + (`$placeEnd`?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String {
        return "RouteSearchRequest(placeStart=" + placeStart + ", placeEnd=" + placeEnd + ")"
    }
}
