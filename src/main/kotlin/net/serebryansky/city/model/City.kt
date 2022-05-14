package net.serebryansky.city.model

class City {
    var id: String? = null
    var name: String? = null
    override fun equals(o: Any?): Boolean {
        if (o === this) return true
        if (o !is City) return false
        val other = o
        if (!other.canEqual(this as Any)) return false
        val `this$id`: Any? = id
        val `other$id`: Any? = other.id
        if (if (`this$id` == null) `other$id` != null else `this$id` != `other$id`) return false
        val `this$name`: Any? = name
        val `other$name`: Any? = other.name
        return if (if (`this$name` == null) `other$name` != null else `this$name` != `other$name`) false else true
    }

    protected fun canEqual(other: Any?): Boolean {
        return other is City
    }

    override fun hashCode(): Int {
        val PRIME = 59
        var result = 1
        val `$id`: Any? = id
        result = result * PRIME + (`$id`?.hashCode() ?: 43)
        val `$name`: Any? = name
        result = result * PRIME + (`$name`?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String {
        return "City(id=" + id + ", name=" + name + ")"
    }
}
