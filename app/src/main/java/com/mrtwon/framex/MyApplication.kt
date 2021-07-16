package com.example.startandroid

import android.app.Application
import androidx.room.Index
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.startandroid.room.Database
import com.mrtwon.framex.Components.AppComponents
import com.mrtwon.framex.Components.DaggerAppComponents

class MyApplication: Application() {
    lateinit var appComponents: AppComponents
    val DB: Database by lazy{
        Room.databaseBuilder(applicationContext, Database::class.java, "database")
            .createFromAsset("database")
            .addMigrations(object: Migration(8,9){
                override fun migrate(database: SupportSQLiteDatabase){}})
            .build()
    }
    override fun onCreate() {
        appComponents = DaggerAppComponents.create()
        getInstance = this
        super.onCreate()
    }
    companion object{
        lateinit var getInstance: MyApplication
    }
}