package net.serebryansky.routing.model

import java.util.concurrent.CopyOnWriteArrayList

class DurationMatrixResponse {
    var matrix: List<List<Double>> = CopyOnWriteArrayList()
    override fun equals(o: Any?): Boolean {
        if (o === this) return true
        if (o !is DurationMatrixResponse) return false
        val other = o
        if (!other.canEqual(this as Any)) return false
        val `this$matrix`: Any = matrix
        val `other$matrix`: Any = other.matrix
        return if (if (`this$matrix` == null) `other$matrix` != null else `this$matrix` != `other$matrix`) false else true
    }

    protected fun canEqual(other: Any?): Boolean {
        return other is DurationMatrixResponse
    }

    override fun hashCode(): Int {
        val PRIME = 59
        var result = 1
        val `$matrix`: Any = matrix
        result = result * PRIME + (`$matrix`?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String {
        return "DurationMatrixResponse(matrix=" + matrix + ")"
    }
}
