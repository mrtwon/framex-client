package com.mrtwon.framex.Retrofit.KinopoiskRating

import com.google.gson.annotations.SerializedName


class RatingPOJO{
    var kp: Double? = null
    var imdb: Double? = null
    override fun toString(): String {
        return "kp $kp | imdb $imdb"
    }
}