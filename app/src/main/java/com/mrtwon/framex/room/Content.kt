package com.mrtwon.framex.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
open class Content {
    @PrimaryKey
    @ColumnInfo(name = "id") var id: Int = 0
    @ColumnInfo(name = "kp_id") var kp_id: Int? = null
    @ColumnInfo(name = "imdb_id") var imdb_id: String? = null
    @ColumnInfo(name = "ru_title")
    open var ru_title: String? = null
    @ColumnInfo(name = "ru_title_lower") var ru_title_lower: String? = null
    @ColumnInfo(name = "description_lower") var description_lower: String? = null
    @ColumnInfo(name = "description") var description: String? = null
    @ColumnInfo(name = "orig_title")  var orig_title: String? = null
    @ColumnInfo(name = "year")  var year: Int? = null
    @ColumnInfo(name = "contentType")  var contentType: String? = null
    @ColumnInfo(name = "poster")  var poster: String? = null
    @ColumnInfo(name = "kp_rating")  var kp_rating: Double? = null
    @ColumnInfo(name = "imdb_rating")  var imdb_rating: Double? = null
    @ColumnInfo(name = "iframe_src") var iframe_src: String? = null
}