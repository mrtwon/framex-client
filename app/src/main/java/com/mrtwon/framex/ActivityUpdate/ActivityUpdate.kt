package com.mrtwon.framex.ActivityUpdate

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import az.plainpie.PieView
import com.google.android.material.button.MaterialButton
import com.mrtwon.framex.R
import com.mrtwon.framex.service.ServiceStatus
import com.mrtwon.framex.service.ServiceUpdate

class ActivityUpdate: AppCompatActivity() {
    lateinit var start: ImageButton
    lateinit var stop: ImageButton
    lateinit var stop_service: ImageButton
    lateinit var help: Button
    lateinit var success: ImageView
    lateinit var disconnect: ImageView
    lateinit var status: TextView
    lateinit var sConn: ServiceConnection
    lateinit var binder: ServiceUpdate
    lateinit var pbUpdate: PieView
    lateinit var pbSearch: ProgressBar
    lateinit var txtStatus: TextView
    var currentStatus: ServiceStatus? = null


    override fun onCreate(savedInstanceState: Bundle?) {


        setContentView(R.layout.activity_update)
        super.onCreate(savedInstanceState)
        stop = findViewById(R.id.pause)
        start = findViewById(R.id.start)
        disconnect = findViewById(R.id.disconnect)
        success = findViewById(R.id.success)
        pbUpdate = findViewById(R.id.pieView)
        pbSearch = findViewById(R.id.progress_bar)
        txtStatus = findViewById(R.id.status)
        stop_service = findViewById(R.id.stop_service)

        settingHtmlForHelperTextView()
        findViewById<MaterialButton>(R.id.btn_update_one_element).setOnClickListener{
            startActivity(Intent(this, ActivityUpdateSearch::class.java))
        }

        stop.setOnClickListener { stop() }
        start.setOnClickListener { start() }
        stop_service.setOnClickListener{ stop_service() }
        sConn = object: ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                //finish()
                log("disconnect")
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                log("connect")
                binder = (service as ServiceUpdate.Binder).getService()
                //Toast.makeText(this@ActivityUpdate, "Connected", Toast.LENGTH_LONG).show()
                observerStatus()
                observerProgress()
                observerCountContentForUpdate()
            }

        }
    }

    override fun onStart() {
        bindService(Intent(this, ServiceUpdate::class.java), sConn, 0)
        super.onStart()
    }
    fun settingHtmlForHelperTextView(){
        log("helper_one: ${getString(R.string.info_for_update)}")
        log("helper_one: ${getString(R.string.info_for_update_only_one_content)}")
        findViewById<TextView>(R.id.tv_helper_one).text = Html.fromHtml("Нажмите кнопку ниже если вы хотите <b>обновить определённый контент</b> который недавно вышел или который не попал в базу")
        findViewById<TextView>(R.id.tv_helper_two).text = Html.fromHtml("Обновляйте всю базу если вы хотите <b>получить актуальный контент</b> который вышел недавно")
    }
    fun observerStatus(){
        //Toast.makeText(this@ActivityUpdate, "Status: ${binder.status.value}", Toast.LENGTH_LONG).show()
        binder.status.observe(this, androidx.lifecycle.Observer {
            when (it) {
                ServiceStatus.CHECK -> {
                    log(it.toString())
                    currentStatus = it
                    stepSearchUpdate()
                }
                ServiceStatus.UPDATE -> {
                    log(it.toString())
                    currentStatus = it
                    stepUpdate()
                }
                ServiceStatus.NOT_UPDATE -> {
                    log(it.toString())
                    currentStatus = it
                    stepNoUpdate()
                }
                ServiceStatus.END_UPDATE -> {
                    log(it.toString())
                    currentStatus = it
                    stepEnd()
                }
                ServiceStatus.STOP -> {
                    currentStatus = it
                    log(it.toString())
                    stepStopped()
                }
                ServiceStatus.DISCONNECT -> {
                    log(it.toString())
                    currentStatus = it
                    stepDisconnect()
                }
                ServiceStatus.STOP_SERVICE -> {
                    log(it.toString())
                    currentStatus = it
                    stepStopService()
                }
            }
        })
    }
    fun observerProgress(){
        binder.progress.observe(this, androidx.lifecycle.Observer {
            pbUpdate.percentage = it.toFloat()
        })
    }
    fun observerCountContentForUpdate(){
        binder.serial.progressIntUpdate.observe(this){
            if(currentStatus == ServiceStatus.CHECK) {
                val countMovie = binder.movie.progressIntUpdate.value ?: 0
                val count = it + countMovie
                txtStatus.text = "Найдено $count"
            }
        }
        binder.movie.progressIntUpdate.observe(this){
            if(currentStatus == ServiceStatus.CHECK) {
                val countSerial = binder.serial.progressIntUpdate.value ?: 0
                val count = it + countSerial
                txtStatus.text = "Найдено $count"
            }
        }
    }

    fun stepSearchUpdate(){
        cleanStep()
        pbSearch.visibility = View.VISIBLE
        txtStatus.text = ServiceStatus.CHECK.description
        stop.isEnabled = true
        stop_service.isEnabled = true
        start.isEnabled = false
    }
    fun stepUpdate(){
        cleanStep()
        pbUpdate.visibility = View.VISIBLE
        txtStatus.text = ServiceStatus.UPDATE.description
        stop.isEnabled = true
        stop_service.isEnabled = true
        start.isEnabled = false
    }
    fun stepStopped(){
        cleanStep()
        success.visibility = View.VISIBLE
        txtStatus.text = ServiceStatus.STOP.description
        stop.isEnabled = false
        start.isEnabled = true
    }
    fun stepEnd(){
        cleanStep()
        success.visibility = View.VISIBLE
        txtStatus.text = ServiceStatus.END_UPDATE.description
        start.isEnabled = true
    }
    fun stepNoUpdate(){
        cleanStep()
        success.visibility = View.VISIBLE
        txtStatus.text = ServiceStatus.NOT_UPDATE.description
        start.isEnabled = true

    }
    fun stepDisconnect(){
        cleanStep()
        disconnect.visibility = View.VISIBLE
        txtStatus.text = ServiceStatus.DISCONNECT.description
        start.isEnabled = false
        stop.isEnabled = false
        stop_service.isEnabled = false
    }
    fun stepStopService(){
        cleanStep()
        txtStatus.text = ServiceStatus.STOP_SERVICE.description
        success.visibility = View.VISIBLE
        start.isEnabled = true
    }
    fun cleanStep(){
        pbUpdate.visibility = View.GONE
        pbSearch.visibility = View.GONE
        success.visibility = View.GONE
        disconnect.visibility = View.GONE
        txtStatus.text = ""
        stop.isEnabled = false
        stop_service.isEnabled = false
    }
    fun stop(){
        binder?.cancelUpdate()
    }
    fun start(){
        if(isMyServiceRunning(ServiceUpdate::class.java)){
            log("service is running")
            binder?.startUpdating()
        }else{
            log("service not running")
            startService(Intent(this, ServiceUpdate::class.java))
            bindService(Intent(this, ServiceUpdate::class.java), sConn, 0)
        }
        //startService(Intent(this, ServiceUpdate::class.java))
        //bindService(Intent(this, ServiceUpdate::class.java), sConn, BIND_AUTO_CREATE)
        start.isEnabled = false
    }
    fun stop_service(){
        binder?.stopService()
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