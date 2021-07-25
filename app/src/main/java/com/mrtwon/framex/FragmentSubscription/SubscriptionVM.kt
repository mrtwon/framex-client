package com.mrtwon.framex.FragmentSubscription

import android.graphics.ColorSpace
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.startandroid.MyApplication
import com.mrtwon.framex.GeneralVM
import com.mrtwon.framex.Model.Model
import com.mrtwon.framex.room.Notification
import com.mrtwon.framex.room.Serial
import com.mrtwon.framex.room.Subscription

class SubscriptionVM: GeneralVM() {
    val notificationListLiveData = model.getNotificationListLiveData()
    private val subscriptionForActionLiveData = model.getSubscriptionListLiveData()
    val subscriptionListLiveData = MutableLiveData<List<Serial>>()

    init {
        subscriptionForActionLiveData.observeForever {
            getSubscription()
        }
    }

    private fun getSubscription(){
        model.getSubscriptionList {
            subscriptionListLiveData.postValue(it)
        }
    }

    fun removeSubscription(id: Int){
        model.removeSubscription(id)
    }

    fun removeNotification(notification: Notification){
        model.removeNotification(notification)
    }

}