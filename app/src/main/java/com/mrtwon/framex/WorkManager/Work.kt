package com.mrtwon.framex.WorkManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mrtwon.framex.Components.AppComponents
import com.mrtwon.framex.Components.DaggerAppComponents
import com.mrtwon.framex.R
import com.mrtwon.framex.Retrofit.VideoCdn.TvSeries.EpisodesItem
import com.mrtwon.framex.Retrofit.VideoCdn.TvSeries.POJOVideoCdnTS
import com.mrtwon.framex.Retrofit.VideoCdn.VideoCdnApi
import com.mrtwon.framex.room.Database
import com.mrtwon.framex.room.Notification
import java.util.*

class Work(private val context: Context, param: WorkerParameters): Worker(context, param) {
    private val NOTIFICATION_FORMAT = "%s %s сезон %s серия уже доступна"
    private var appComponents: AppComponents = DaggerAppComponents.create()
    private var database: Database = appComponents.getDatabase()
    private var videoCdn: VideoCdnApi = appComponents.getVideoCdnApi()


    override fun doWork(): Result {
        val resultStepOne = stepOne() // give pojo + startId
        val resultStepTwo = stepTwo(resultStepOne) // give notification list
        stepThree(resultStepTwo) // send notification
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
    /*fun stepOne(){
           log("started")
           val subscriptList = database.dao().getSubscriptions()
            for(subscript in subscriptList){
                val response = videoCdn.serialById(subscript.content_id).execute().body()
                val episodeList = response?.data?.get(0)?.episodes
                val ru_title = response?.data?.get(0)?.ruTitle ?: continue
                val size = response.data[0]?.episodeCount ?: continue
                log("all size serial = $size | my size = ${subscript.count}")
                if(size > subscript.count){
                    stepTwo(
                        getNotificationFromData(episodeList, subscript.count, size, subscript.content_id, ru_title)
                    )
                    subscript.count = size
                    database.dao().updateSubscription(subscript)
                }
                *//*else{
                    createNotification("Нет новых серий для $ru_title", Random().nextInt(5000))
                }*//*
           }
    }
    private fun stepTwo(list: List<Notification>?){
        list?.let {
            database.dao().insertNotifications(it)
            startNotification(it)
        }
        log("end")
    }
    fun startNotification(listNotification: List<Notification>){
        createNotificationChannel()
        val random = Random()
        for(notification in listNotification){
            val message = "${notification.ru_title} ${notification.season} сезон ${notification.series} серия уже доступна"
            val randomIdNotification = random.nextInt(5000)
            log("random id = $randomIdNotification")
            createNotification(message)
        }
    }
    private fun getNotificationFromData(epList: List<EpisodesItem?>?, startId: Int, endId: Int, serialId: Int, ru_title: String): List<Notification>?{
        if(epList == null) return null
        log("[getNotificationFromData] startId $startId endId $endId")
        val notificationList = arrayListOf<Notification>()
        for(index in startId until endId){
            val notification = Notification()
            val episode = epList[index]
            if (episode != null) {
                if(episode.seasonNum != null && episode.num != null && episode.ruTitle != null){
                    notification.content_id = serialId
                    notification.season = episode.seasonNum.toString()
                    notification.series = episode.num
                    notification.ru_title = ru_title
                    notificationList.add(notification)
                    log("[index $index][content_id ${notification.content_id}] add episode ${notification.season}x${notification.series}")
                }
            }
        }
        return notificationList
    }
*/
    fun createNotification(text: String){
        val id = Random().nextInt(5000)
        Log.i("self-main","createNotifty()")
        val builder = NotificationCompat.Builder(context, "101")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Новая серия")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .build()
        val manager = NotificationManagerCompat.from(context)
        manager.notify(id, builder)
    }
    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "New Content"
            val channel = NotificationChannel("101", name, NotificationManager.IMPORTANCE_DEFAULT)
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    fun log(s: String){
        Log.i("self-worker", s)
    }

    inner class NotificationWrapper(val pojo: POJOVideoCdnTS, val startId: Int)

}