package com.mrtwon.framex.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Subscription {
    @PrimaryKey var id: Int? = null
    @ColumnInfo(name = "content_id") var content_id: Int = 0
    @ColumnInfo(name = "count") var count: Int = 0
}