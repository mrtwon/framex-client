package com.mrtwon.framex.WorkManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mrtwon.framex.Components.AppComponents
import com.mrtwon.framex.Components.DaggerAppComponents
import com.mrtwon.framex.MainActivity
import com.mrtwon.framex.R
import com.mrtwon.framex.Retrofit.VideoCdn.TvSeries.EpisodesItem
import com.mrtwon.framex.Retrofit.VideoCdn.TvSeries.POJOVideoCdnTS
import com.mrtwon.framex.Retrofit.VideoCdn.VideoCdnApi
import com.mrtwon.framex.room.Database
import com.mrtwon.framex.room.Notification
import java.util.*

class OldWork(private val context: Context, param: WorkerParameters): Worker(context, param) {
    private val NOTIFICATION_FORMAT = "%s %s сезон %s серия уже доступна"
    private lateinit var PENDING_INTENT: PendingIntent
    private var appComponents: AppComponents = DaggerAppComponents.create()
    private var database: Database = appComponents.getDatabase()
    private var videoCdn: VideoCdnApi = appComponents.getVideoCdnApi()

    override fun doWork(): Result {
        val intent = Intent(context, MainActivity::class.java).putExtra("redirect", "FragmentSubscription")
        PENDING_INTENT = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        Log.i("self-worker","start")
        val resultStepOne = stepOne() // give pojo + startId
        val resultStepTwo = stepTwo(resultStepOne) // give notification list
        stepThree(resultStepTwo) // send notification
        Log.i("self-worker","end")
        return Result.success()

    }
    private fun stepOne(): List<NotificationWrapper>{
        val subscriptions = database.dao().getSubscriptions()   // all episode for subscription
        val listNotificationWrapper = arrayListOf<NotificationWrapper>()  // create notification wrapper
        for(subscript in subscriptions){
            val response = videoCdn.serialById(subscript.content_id).execute().body()  // serial-information by id
            val episodeCount = response?.data?.get(0)?.episodeCount ?: continue
            if(episodeCount > subscript.count){
                listNotificationWrapper.add(NotificationWrapper(response, subscript.count))
                subscript.count = episodeCount
                database.dao().updateSubscription(subscript)   // update last episode
            }
        }
        return listNotificationWrapper
    }
    private fun stepTwo(notificationWrappers: List<NotificationWrapper>): List<Notification>{
        val notificationList = arrayListOf<Notification>()
        for(element in notificationWrappers){  // отдельный сериал
            val data = element.pojo.data?.get(0) ?: continue
            val title = data.ruTitle ?: continue
            val id = data.id ?: continue
            val episodeList = data.episodes ?: continue


            val sublist = episodeList.subList(element.startId, episodeList.size)
            for(episode in sublist){ // эпизоды сериала
                val notification = Notification()
                notification.content_id = id
                notification.ru_title = title
                notification.season = episode?.seasonNum.toString()
                notification.series = episode?.num ?: continue
                notificationList.add(notification)
            }
        }
        return notificationList
    }
    private fun stepThree(notificationList: List<Notification>){
        if(notificationList.isNotEmpty()){
            createNotificationChannel()
            for(notification in notificationList){
                val text = String.format(NOTIFICATION_FORMAT, notification.ru_title, notification.season, notification.series)
                createNotification(text)
            }
            database.dao().insertNotifications(notificationList)
        }
    }
    private fun createNotification(text: String){
        val id = Random().nextInt(5000)
        Log.i("self-main","createNotifty()")
        val builder = NotificationCompat.Builder(context, "101")
            .setSmallIcon(R.mipmap.icon_app)
            .setContentTitle("Новая серия")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(PENDING_INTENT)
            .build()
        val manager = NotificationManagerCompat.from(context)
        manager.notify(id, builder)
    }
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "New Content"
            val channel = NotificationChannel("101", name, NotificationManager.IMPORTANCE_DEFAULT)
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    private fun log(s: String){
        Log.i("self-worker", s)
    }

    inner class NotificationWrapper(val pojo: POJOVideoCdnTS, val startId: Int)

}