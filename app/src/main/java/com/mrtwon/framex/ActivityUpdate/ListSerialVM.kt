package com.mrtwon.framex.ActivityUpdate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtwon.framex.Model.ModelApi
import com.mrtwon.framex.room.Movie
import com.mrtwon.framex.room.Serial

class ListSerialVM: ViewModel() {
    val modelApi = ModelApi()
    val serialList = MutableLiveData<List<Serial>>()
    val visibilityPb = MutableLiveData<Boolean>()
    val noConnect = MutableLiveData<Boolean>()
    fun searchSerial(query: String){
        visibilityPb.postValue(true)
        modelApi.searchSerial(query, {
            visibilityPb.postValue(false)
            serialList.postValue(it)
        },{
            noConnect.postValue(it)
        })
    }
}