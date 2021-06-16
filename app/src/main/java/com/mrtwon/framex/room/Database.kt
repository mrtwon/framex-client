package com.example.startandroid.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mrtwon.framex.room.*

@Database(entities = arrayOf(
    Movie::class, Serial::class, CountriesMovie::class,
    GenresMovie::class, Genres::class, Countries::class,
    Favorite::class, Recent::class
), version = 9)
abstract class Database: RoomDatabase(){
    //getting dao for requesting
    abstract fun dao(): Dao
}