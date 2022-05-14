package net.serebryansky.builder.model

import java.util.concurrent.CopyOnWriteArrayList

class TripBuilderResponse {
    var placeIds: List<String?> = CopyOnWriteArrayList()
    override fun equals(o: Any?): Boolean {
        if (o === this) return true
        if (o !is TripBuilderResponse) return false
        val other = o
        if (!other.canEqual(this as Any)) return false
        val `this$placeIds`: Any = placeIds
        val `other$placeIds`: Any = other.placeIds
        return if (if (`this$placeIds` == null) `other$placeIds` != null else `this$placeIds` != `other$placeIds`) false else true
    }

    protected fun canEqual(other: Any?): Boolean {
        return other is TripBuilderResponse
    }

    override fun hashCode(): Int {
        val PRIME = 59
        var result = 1
        val `$placeIds`: Any = placeIds
        result = result * PRIME + (`$placeIds`?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String {
        return "TripBuilderResponse(placeIds=" + placeIds + ")"
    }
}
