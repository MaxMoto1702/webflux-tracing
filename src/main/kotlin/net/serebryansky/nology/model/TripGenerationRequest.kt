package net.serebryansky.nology.model

class TripGenerationRequest {
    var cityId: String? = null
    override fun equals(o: Any?): Boolean {
        if (o === this) return true
        if (o !is TripGenerationRequest) return false
        val other = o
        if (!other.canEqual(this as Any)) return false
        val `this$cityId`: Any? = cityId
        val `other$cityId`: Any? = other.cityId
        return if (if (`this$cityId` == null) `other$cityId` != null else `this$cityId` != `other$cityId`) false else true
    }

    protected fun canEqual(other: Any?): Boolean {
        return other is TripGenerationRequest
    }

    override fun hashCode(): Int {
        val PRIME = 59
        var result = 1
        val `$cityId`: Any? = cityId
        result = result * PRIME + (`$cityId`?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String {
        return "TripGenerationRequest(cityId=" + cityId + ")"
    }
}
