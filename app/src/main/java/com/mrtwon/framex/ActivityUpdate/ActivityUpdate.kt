package com.mrtwon.framex.ActivityUpdate

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import az.plainpie.PieView
import com.mrtwon.framex.R
import com.mrtwon.framex.service.ServiceStatus
import com.mrtwon.framex.service.ServiceUpdate

class ActivityUpdate: AppCompatActivity() {
    lateinit var start: ImageButton
    lateinit var stop: ImageButton
    lateinit var pause: Button
    lateinit var help: Button
    lateinit var success: ImageView
    lateinit var disconnect: ImageView
    lateinit var status: TextView
    lateinit var sConn: ServiceConnection
    lateinit var binder: ServiceUpdate
    lateinit var pbUpdate: PieView
    lateinit var pbSearch: ProgressBar
    lateinit var txtStatus: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO проценты в ходе остановы срабатывают некорректно вверх.
        //TODO numberFormatException в Movie (входные данные 2020-2020)

        setContentView(R.layout.activity_update)
        super.onCreate(savedInstanceState)
        stop = findViewById(R.id.pause)
        start = findViewById(R.id.start)
        disconnect = findViewById(R.id.disconnect)
        success = findViewById(R.id.success)
        pbUpdate = findViewById(R.id.pieView)
        pbSearch = findViewById(R.id.progress_bar)
        txtStatus = findViewById(R.id.status)

        //for testing

        /*findViewById<Button>(R.id.update).setOnClickListener { binder.status.postValue(ServiceStatus.UPDATE) }
        findViewById<Button>(R.id.not_update).setOnClickListener { binder.status.postValue(ServiceStatus.NOT_UPDATE) }
        findViewById<Button>(R.id.check).setOnClickListener { binder.status.postValue(ServiceStatus.CHECK) }
        findViewById<Button>(R.id.disconnect_network).setOnClickListener { binder.status.postValue(ServiceStatus.DISCONNECT) }
        findViewById<Button>(R.id.stop_update).setOnClickListener { binder.status.postValue(ServiceStatus.STOP) }
        findViewById<Button>(R.id.end_update).setOnClickListener { binder.status.postValue(ServiceStatus.END_UPDATE) }*/

        // end testing


        stop.setOnClickListener { stop() }
        start.setOnClickListener { start() }
        sConn = object: ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                //finish()
                log("disconnect")
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                log("connect")
                binder = (service as ServiceUpdate.Binder).getService()
                Toast.makeText(this@ActivityUpdate, "Connected", Toast.LENGTH_LONG).show()
                observerStatus()
                observerProgress()
            }

        }
    }

    override fun onStart() {
        bindService(Intent(this, ServiceUpdate::class.java), sConn, 0)
        super.onStart()
    }
    fun observerStatus(){
        Toast.makeText(this@ActivityUpdate, "Status: ${binder.status.value}", Toast.LENGTH_LONG).show()
        binder.status.observe(this, androidx.lifecycle.Observer {
            when (it) {
                ServiceStatus.CHECK -> {
                    log(it.toString())
                    stepSearchUpdate()
                }
                ServiceStatus.UPDATE -> {
                    log(it.toString())
                    stepUpdate()
                }
                ServiceStatus.NOT_UPDATE -> {
                    log(it.toString())
                    stepNoUpdate()
                }
                ServiceStatus.END_UPDATE -> {
                    log(it.toString())
                    stepEnd()
                }
                ServiceStatus.STOP -> {
                    log(it.toString())
                    stepStopped()
                }
                ServiceStatus.DISCONNECT -> {
                    log(it.toString())
                    stepDisconnect()
                }
            }
        })
    }
    fun observerProgress(){
        binder.progress.observe(this, androidx.lifecycle.Observer {
            pbUpdate.percentage = it.toFloat()
        })
    }

    fun stepSearchUpdate(){
        cleanStep()
        pbSearch.visibility = View.VISIBLE
        txtStatus.text = "Поиск Обновлений ..."
        stop.isEnabled = true
        start.isEnabled = false
    }
    fun stepUpdate(){
        cleanStep()
        pbUpdate.visibility = View.VISIBLE
        txtStatus.text = "Обновление Базы Данных ..."
        stop.isEnabled = true
        start.isEnabled = false
    }
    fun stepStopped(){
        cleanStep()
        success.visibility = View.VISIBLE
        txtStatus.text = "Обновление Остановленно."
        stop.isEnabled = false
        start.isEnabled = true
    }
    fun stepEnd(){
        cleanStep()
        success.visibility = View.VISIBLE
        txtStatus.text = "База данных обновленна."
    }
    fun stepNoUpdate(){
        cleanStep()
        success.visibility = View.VISIBLE
        txtStatus.text = "Обновлений нет."
    }
    fun stepDisconnect(){
        cleanStep()
        disconnect.visibility = View.VISIBLE
        txtStatus.text = "Проверьте доступ к сети"
        start.isEnabled = false
        stop.isEnabled = false
    }
    fun cleanStep(){
        pbUpdate.visibility = View.GONE
        pbSearch.visibility = View.GONE
        success.visibility = View.GONE
        disconnect.visibility = View.GONE
        txtStatus.text = ""
        stop.isEnabled = false
    }
    fun stop(){
        binder?.cancelUpdate()
    }
    fun start(){
        if(isMyServiceRunning(ServiceUpdate::class.java)){
            binder?.startUpdating()
        }else{
            startService(Intent(this, ServiceUpdate::class.java))
        }
        start.isEnabled = false
    }
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
    fun log(msg: String){
        Log.i("connect-service", msg)
    }
}