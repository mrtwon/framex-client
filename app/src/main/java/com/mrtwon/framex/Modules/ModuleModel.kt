package com.mrtwon.framex.Modules

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.startandroid.MyApplication
import com.example.testbook.Retrofit.Kinopoisk.KinopoiskApi
import com.github.mrtwon.library.IgnoreCode
import com.github.mrtwon.library.XmlParse
import com.github.mrtwon.library.XmlParseBuilder
import com.mrtwon.framex.Model.Model
import com.mrtwon.framex.Retrofit.VideoCdn.VideoCdnApi
import com.mrtwon.framex.room.Database
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ModuleModel {

   @Singleton
   @Provides
   fun getModel(db: Database, videoCdn: VideoCdnApi, kp: KinopoiskApi, rating: XmlParse): Model{
      return Model(db, kp, videoCdn, rating)
   }

   @Singleton
   @Provides
   fun getDatabase(): Database {
      return Room.databaseBuilder(MyApplication.getInstance.applicationContext,
         Database::class.java, "database")
         .createFromAsset("database")
         .addMigrations(object: Migration(10,11){
            override fun migrate(database: SupportSQLiteDatabase){
            /*  Migration Action */
            /*database.execSQL("""
               CREATE TABLE subscription (
                   id         INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                   content_id INTEGER,
                   count      INTEGER
               );
               CREATE TABLE notification (
                   id         INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                   content_id INTEGER,
                   season     TEXT,
                   series     TEXT
               );
            """.trimIndent())*/
            }})
         .build()
   }
}