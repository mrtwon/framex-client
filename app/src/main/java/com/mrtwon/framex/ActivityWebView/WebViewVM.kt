package com.mrtwon.framex.ActivityWebView

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex.GeneralVM
import com.mrtwon.framex.room.Content

class WebViewVM: GeneralVM() {
    val content = MutableLiveData<Content>()
    fun getVideoLink(id: Int, contentType: String){
        model.getVideoLink(id, contentType){
            content.postValue(it)
        }
    }
    fun addRecent(id: Int, contentType: String){
        model.addRecent(id, contentType)
    }
}