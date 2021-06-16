package com.mrtwon.framex.room

import androidx.room.Entity

@Entity
open class MovieWithGenres: Movie() {
    open var genres: String? = null
}