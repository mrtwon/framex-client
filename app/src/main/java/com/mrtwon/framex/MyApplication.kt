package com.example.startandroid

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.mrtwon.framex.room.Database
import com.mrtwon.framex.Components.AppComponents
import com.mrtwon.framex.Components.DaggerAppComponents
import com.mrtwon.framex.WorkManager.Work
import java.util.concurrent.TimeUnit

class MyApplication: Application() {
    lateinit var appComponents: AppComponents
    override fun onCreate() {
        appComponents = DaggerAppComponents.create()
        getInstance = this
        startWorkManager()
        super.onCreate()
    }

    fun startWorkManager(){
        val instance = WorkManager.getInstance(applicationContext)
        val workInfo = instance.getWorkInfosByTag("subscription")
        if(workInfo.get().isEmpty()){
            Log.i("self-about","WokManager started")
            val myRequest = PeriodicWorkRequest.Builder(Work::class.java, 15, TimeUnit.MINUTES)
                .addTag("subscription")
                .build()
            instance.enqueue(myRequest)
        }
    }
    companion object{
        lateinit var getInstance: MyApplication
    }
}