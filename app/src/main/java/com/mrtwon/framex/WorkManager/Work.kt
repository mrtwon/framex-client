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
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mrtwon.framex.Components.AppComponents
import com.mrtwon.framex.Components.DaggerAppComponents
import com.mrtwon.framex.MainActivity
import com.mrtwon.framex.R
import com.mrtwon.framex.Retrofit.VideoCdn.TvSeries.POJOVideoCdnTS
import com.mrtwon.framex.Retrofit.VideoCdn.VideoCdnApi
import com.mrtwon.framex.room.Database
import com.mrtwon.framex.room.Notification
import kotlinx.coroutines.*
import java.util.*

class Work(private val context: Context, param: WorkerParameters): Worker(context, param) {
    private val NOTIFICATION_FORMAT = "%s %s сезон %s серия уже доступна"
    private lateinit var PENDING_INTENT: PendingIntent
    private var appComponents: AppComponents = DaggerAppComponents.create()
    private var database: Database = appComponents.getDatabase()
    private var videoCdn: VideoCdnApi = appComponents.getVideoCdnApi()

    init {
        val intent = Intent(context, MainActivity::class.java).putExtra("redirect", "FragmentSubscription")
        PENDING_INTENT = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun doWork(): Result {
        stepStart()
        return Result.success()

    }
    private fun stepStart() {
        val scope = CoroutineScope(Job() + Dispatchers.IO + CoroutineExceptionHandler { _, _ -> {} })
        scope.launch {
            createNotificationChannel()
            val subscriptions = database.dao().getSubscriptions()   // all episode for subscription
            for (subscript in subscriptions) {
                launch {
                    Log.i("self-worker","start processing ...")
                    val response = videoCdn.serialById(subscript.content_id).execute().body()  // serial-information by id
                    val episodeCount = response?.data?.get(0)?.episodeCount
                    if (episodeCount != null && episodeCount > subscript.count) {
                        val wrapper = NotificationWrapper(response, subscript.count)
                        subscript.count = episodeCount
                        database.dao().updateSubscription(subscript)   // update last episode
                        stepTwo(wrapper)
                    }
                }
            }
        }
    }

    private fun stepTwo(notificationWrapper: NotificationWrapper){
        val data = notificationWrapper.pojo.data?.get(0) ?: return
        val title = data.ruTitle ?: return
        val id = data.id ?: return
        val episodeList = data.episodes ?: return


        val sublist = episodeList.subList(notificationWrapper.startId, episodeList.size)
        for (episode in sublist) { // эпизоды сериала
                val notification = Notification()
                notification.content_id = id
                notification.ru_title = title
                notification.season = episode?.seasonNum?.toString() ?: return
                notification.series = episode.num ?: return
                stepThree(notification)
            }
        }

    private fun stepThree(notification: Notification){
                Log.i("self-worker", "add to database")
                val text = String.format(
                    NOTIFICATION_FORMAT,
                    notification.ru_title,
                    notification.season,
                    notification.series
                )
                createNotification(text)
            database.dao().insertNotification(notification)
        }


    private fun createNotification(text: String) {
        val id = Random().nextInt(5000)
        Log.i("self-main", "createNotifty()")
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "New Content"
            val channel = NotificationChannel("101", name, NotificationManager.IMPORTANCE_DEFAULT)
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun log(s: String) {
        Log.i("self-worker", s)
    }

    inner class NotificationWrapper(val pojo: POJOVideoCdnTS, val startId: Int)

}