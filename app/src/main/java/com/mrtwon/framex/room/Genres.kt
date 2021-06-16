package com.mrtwon.framex.room
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Genres: OnlyGenres() {
    @PrimaryKey(autoGenerate = true) var id: Int? = null
    var kp_id: Int? = null
    var imdb_id: String? = null
}