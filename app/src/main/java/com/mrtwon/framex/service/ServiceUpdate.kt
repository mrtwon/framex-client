package com.mrtwon.framex.service

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.lifecycle.MutableLiveData
import com.example.startandroid.MyApplication
import com.mrtwon.framex.ActivityUpdate.ActivityUpdate
import com.mrtwon.framex.R
/*import com.example.startandroid.retrofit.InstanceApi
import com.example.startandroid.retrofit.KinopoiskRating.RatingPOJO
import com.example.startandroid.room.Movie
import com.example.testbook.Retrofit.Kinopoisk.POJOKinopoisk
import com.example.testbook.Retrofit.VideoCdn.Movies.DataItem
import com.example.testbook.Retrofit.VideoCdn.Movies.POJOVideoCdnMovie*/
import kotlinx.coroutines.*

class ServiceUpdate: Service() {
    val bind = Binder()
    val status =  MutableLiveData<ServiceStatus>()
    val progress = MutableLiveData<Int>()
    private val CHANNEL = "my_service"
    private val CH_ID = 1
    var successful = 0
    var job = Job()
    lateinit var scope: CoroutineScope
    val serial = SerialUpdate()
    val movie = MovieUpdate()
    var networkCallback: ConnectivityManager.NetworkCallback? = null
    var connectManager: ConnectivityManager? = null
    val handler = CoroutineExceptionHandler{ context , exception ->
        //log("EXCEPTION ${exception.printStackTrace()}")
        exception.printStackTrace()
    }
    override fun onBind(intent: Intent?): IBinder? {
        return bind
    }
    override fun onCreate() {
        log("onCreate")
        scope = CoroutineScope(Dispatchers.Default + Job() + handler)
        monitorActive()
        super.onCreate()
    }

    override fun onDestroy() {
        networkCallback?.let {
            connectManager?.unregisterNetworkCallback(it)
        }
        job.cancel()
        super.onDestroy()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("startId = $startId")
        if(startId == 1) {
            startForeground()
            startUpdating()
        }
        return super.onStartCommand(intent, flags, startId)
    }
    fun startUpdating(){
        log("startUpdating fun started")
        job = Job()
        scope = CoroutineScope(Dispatchers.Default + job + handler)
        serial.scope = scope
        movie.scope = scope
        scope.launch {
            // TODO посмотреть сколько трафик уходит
            notifyWaitIsUpdate()  // проверка обновления
            status.postValue(ServiceStatus.CHECK)
            delay(3000)
            val countSerial = serial.forUpdate().size // проверка сериалов
            val countMovie = movie.forUpdate().size // проверка фильмов
            val sumCount = countMovie + countSerial // суммарное кол-во

            isUpdateToast(sumCount)  // toast об кол-ве обновлений
            if(sumCount == 0){
                status.postValue(ServiceStatus.NOT_UPDATE)
                stopSelf()
                job.cancel()
            }
            //delay(2000)
            ensureActive()
            status.postValue(ServiceStatus.UPDATE)
            val isFinishSerial = serial.updateDatabase{   // обновляем сериалы
                incrementProgress()
            }
            ensureActive()
            val isFinishMovie = movie.updateDatabase{   // обновляем фильмы
                incrementProgress()
            }
            ensureActive()
            status.postValue(ServiceStatus.END_UPDATE)
            //successful = 0 // сброс счётчика
            stopSelf()
        }
    }

    fun cancelUpdate(){
        job.cancel()
        status.postValue(ServiceStatus.STOP)
    }
    fun stopService(){
        status.postValue(ServiceStatus.STOP_SERVICE)
        job.cancel()
        stopSelf()
    }

    @Synchronized
    fun incrementProgress(){
            successful+=1
            val maxProgress = serial.contentForProgress.size + movie.contentForProgress.size
            val currentProgress = ((successful.toFloat()/maxProgress.toFloat())*100).toInt()
            updateNotify(currentProgress)
            progress.postValue(currentProgress)
    }

    fun monitorActive() {
        connectManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                notifyDisconnect()
                status.postValue(ServiceStatus.DISCONNECT)
                super.onLost(network)
            }
            override fun onAvailable(network: Network) {
                if(!scope.isActive){
                    Toast.makeText(MyApplication.getInstance.applicationContext,
                        "Возобновления обновлений", Toast.LENGTH_LONG).show()
                    startUpdating()
                }
                super.onAvailable(network)
            }
        }
        networkCallback?.let {
            connectManager?.registerDefaultNetworkCallback(it)
        }
    }

    fun isUpdateToast(count: Int){
        val toastMessage = when{
            count > 0 -> "Доступно Обновления. $count"
            else -> "Обновлений нет."
        }
        scope.launch(Dispatchers.Main){
            Toast.makeText(
                MyApplication.getInstance.applicationContext, toastMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun startForeground(): RemoteViews {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }
        val remoteView = RemoteViews(packageName, R.layout.layout_wait)
        val notification = NotificationCompat.Builder(this, channelId )
            .setOngoing(true)
            .setSmallIcon(R.drawable.movie)
            .setCustomContentView(remoteView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(PendingIntent.getActivity(this, 0, Intent(this, ActivityUpdate::class.java), 0))
            .build()
        startForeground(CH_ID, notification)
        return remoteView
    }

    private fun notifyWaitIsUpdate(){
        val remoteView = RemoteViews(packageName, R.layout.layout_wait)
        val notification = NotificationCompat.Builder(this, CHANNEL )
            .setOngoing(true)
            .setSmallIcon(R.drawable.movie)
            .setCustomContentView(remoteView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(PendingIntent.getActivity(this, 0, Intent(this, ActivityUpdate::class.java), 0))
            .build()
        val service = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        service.notify(CH_ID, notification)
    }
    private fun notifyDisconnect(){
        val remoteView = RemoteViews(packageName, R.layout.layout_disconnect)
        val notification = NotificationCompat.Builder(this, CHANNEL )
            .setOngoing(true)
            .setSmallIcon(R.drawable.movie)
            .setCustomContentView(remoteView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(PendingIntent.getActivity(this, 0, Intent(this, ActivityUpdate::class.java), 0))
            .build()
        val service = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        service.notify(CH_ID, notification)
    }
    private fun updateNotify(progress: Int){
        val remoteView = RemoteViews(packageName, R.layout.layout_notify)
        remoteView.setTextViewText(R.id.update, "Обновление. $progress%")
        remoteView.setProgressBar(R.id.progressBar, 100, progress, true)
        val notification = NotificationCompat.Builder(this, CHANNEL )
            .setOngoing(true)
            .setSmallIcon(R.drawable.movie)
            .setCustomContentView(remoteView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(PendingIntent.getActivity(this, 0, Intent(this, ActivityUpdate::class.java), 0))
            .build()
        val service = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        service.notify(CH_ID, notification)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE).apply {
            lightColor = Color.BLUE
            lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        }
        val service = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    fun log(s: String){
        Log.i("self-service",s)
    }

    inner class Binder: android.os.Binder(){
        fun getService(): ServiceUpdate{
            return this@ServiceUpdate
        }
    }
}