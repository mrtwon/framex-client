package com.mrtwon.framex.ActivityUpdate

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex.GeneralVM
import com.mrtwon.framex.room.Serial

class ListSerialVM: GeneralVM() {
    val serialList = MutableLiveData<List<Serial>>()
    val visibilityPb = MutableLiveData<Boolean>()
    val noConnect = MutableLiveData<Boolean>()
    fun searchSerial(query: String){
        visibilityPb.postValue(true)
        model.searchSerial(query, {
            visibilityPb.postValue(false)
            serialList.postValue(it)
        },{
            noConnect.postValue(it)
        })
    }
}