package org.hango.com.Data

class Travel {
    var city: String? = null
    var spot: String? = null
    var img: String? = null
    var mapX: Double? = null
    var mapY: Double? = null

    constructor(city: String?, spot: String?, img: String?) {
        this.city = city
        this.spot = spot
        this.img = img
    }

    constructor() {}

    fun setAddress(addr: String?) {
        city = addr
    }

    fun setImage(img: String?) {
        this.img = img
    }
}