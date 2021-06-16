package com.example.startandroid

import android.app.Application
import androidx.room.Index
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.startandroid.room.Database

class MyApplication: Application() {
    val DB: Database by lazy{
        Room.databaseBuilder(applicationContext, Database::class.java, "database")
            .addMigrations(object: Migration(8,9){
                override fun migrate(database: SupportSQLiteDatabase) {
                    /*with(database) {
                        execSQL("CREATE INDEX idx_movie_kp_id ON Movie(kp_id)")
                        execSQL("CREATE INDEX idx_serial_kp_id ON Serial(kp_id)")
                        execSQL("CREATE TABLE Favorite (\n" +
                                            "    id          INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                                            "                        ,\n" +
                                            "    id_ref      INTEGER,\n" +
                                            "    contentType TEXT\n" +
                                            ");\n")
                        execSQL("CREATE TABLE Recent (\n" +
                                            "    id          INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                                            "                        ,\n" +
                                            "    id_ref      INTEGER,\n" +
                                            "    contentType TEXT,\n" +
                                            "    time        INTEGER\n" +
                                            ");")
                    }*/
                }
            }).build()
    }
    override fun onCreate() {
        getInstance = this
        super.onCreate()
    }
    companion object{
        lateinit var getInstance: MyApplication
    }
}