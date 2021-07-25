package com.mrtwon.framex

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mrtwon.framex.ActivityWelcome.ActivityWelcome
import java.io.File

class StartedActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.empty)
        Log.i("self-started", "db existing - ${isExist()}")
        if(isExist()){
            startActivity(Intent(this, MainActivity::class.java))
            //startActivity(Intent(this, TestWorker::class.java))
            finish()
        }else{
            startActivity(Intent(this, ActivityWelcome::class.java))
            finish()
        }
        //startActivity(Intent(this, ActivityWelcome::class.java))
        super.onCreate(savedInstanceState)
    }
    @SuppressLint("SdCardPath")
    fun isExist(): Boolean{
        val file = File("/data/data/com.mrtwon.framex/databases/database")
        return file.exists()
    }
}