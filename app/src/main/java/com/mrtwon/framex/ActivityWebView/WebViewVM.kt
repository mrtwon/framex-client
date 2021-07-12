package com.mrtwon.framex.ActivityWebView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtwon.framex.Model.ModelDatabase
import com.mrtwon.framex.room.Content

class WebViewVM: ViewModel() {
    private val model = ModelDatabase()
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