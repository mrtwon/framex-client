package com.mrtwon.framex.room

import androidx.room.Entity

@Entity
open class GenresMovie: Genres() {
    override fun toString(): String {
        return "[${kp_id}]    ${genres}"
    }
}