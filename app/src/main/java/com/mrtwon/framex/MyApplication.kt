package com.example.startandroid

import android.app.Application
import android.util.Log
import androidx.work.*
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

            val constraint = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val myRequest = PeriodicWorkRequest.Builder(Work::class.java, 15, TimeUnit.MINUTES)
                .addTag("subscription")
                .setConstraints(constraint)
                .build()

            instance.enqueue(myRequest)

        }
    }
    companion object{
        lateinit var getInstance: MyApplication
    }
}