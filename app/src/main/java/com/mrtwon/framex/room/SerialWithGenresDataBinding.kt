package com.mrtwon.framex.room

import com.mrtwon.framex.Helper.HelperFunction

class SerialWithGenresDataBinding(val serial: SerialWithGenres){
    val NO_DATA: String = "Нет данных."
    var genres: String = serial.genres ?: NO_DATA
        get() = "($field)"
    var ru_title: String = serial.ru_title ?: NO_DATA
        get() = if(serial.year != null) "$field  (${serial.year})" else field
    var description: String = serial.description ?: NO_DATA
    var kp_rating: String = HelperFunction.roundRating(serial.kp_rating)
    var imdb_rating: String = HelperFunction.roundRating(serial.imdb_rating)
}