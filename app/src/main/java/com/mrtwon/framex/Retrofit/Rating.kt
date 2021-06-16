package com.mrtwon.framex.Retrofit

class Rating {
    var imdb: String? = null
    var kp: String? = null
        set(value) {
            if(value != null && value.length >= 4) {
                field = value.substring(0, 3)
            }else{
                field = value
            }
        }
    override fun toString(): String {
        return "IMDB $imdb KinoPoisk $kp"
    }
}