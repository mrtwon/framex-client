package com.mrtwon.framex.ActivityUpdate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtwon.framex.Model.ModelApi
import com.mrtwon.framex.room.Serial

class SerialUpdateVM: ViewModel() {
    val model = ModelApi()
    val listSerial = MutableLiveData<List<Serial>>()
    fun search(query: String){
        model.searchSerial(query){
            listSerial.postValue(it)
        }
    }
}