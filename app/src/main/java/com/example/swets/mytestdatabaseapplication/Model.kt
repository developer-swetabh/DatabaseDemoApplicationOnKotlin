package com.example.swets.mytestdatabaseapplication

/**
 * Created by swets on 07-01-2016.
 */
class Model {
    var id: Int = 0
    var name: String? = null
    var address: String? = null

    override fun toString(): String {
        return name + " " + id.toString() + " " + address
    }
}
