package com.mrtwon.framex.room

class MovieWithGenresDataBinding(val movie: MovieWithGenres){
    val NO_DATA: String = "Нет данных."
    var genres: String = movie.genres ?: NO_DATA
        get() = "($field)"
    var ru_title: String = movie.ru_title ?: NO_DATA
        get() = if(movie.year != null) "$field  (${movie.year})" else field
    var description: String = movie.description ?: NO_DATA
    var kp_rating: String = movie.kp_rating?.toString() ?: NO_DATA
    var imdb_rating: String = movie.imdb_rating?.toString() ?: NO_DATA
}