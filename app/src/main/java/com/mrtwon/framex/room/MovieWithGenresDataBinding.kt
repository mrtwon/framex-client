package com.mrtwon.framex.room

import android.icu.math.BigDecimal
import com.mrtwon.framex.Helper.HelperFunction.Companion.roundRating

class MovieWithGenresDataBinding(val movie: MovieWithGenres){
    val NO_DATA: String = "Нет данных."
    val NO_DESCRIPTION: String = "Описание отсутствует"
    var genres: String = movie.genres ?: NO_DATA
        get() = "($field)"
    var ru_title: String = movie.ru_title ?: NO_DATA
        get() = if(movie.year != null) "$field  (${movie.year})" else field
    var description: String = movie.description ?: NO_DESCRIPTION
    var kp_rating: String = roundRating(movie.kp_rating)
    var imdb_rating: String = roundRating(movie.imdb_rating)
}