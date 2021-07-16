package com.mrtwon.framex

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtwon.framex.room.Content
import com.mrtwon.framex.room.DatabaseSize

class MainViewModel: GeneralVM() {
    val listRecent = MutableLiveData<List<Content>>()
    val dbSize = MutableLiveData<DatabaseSize>()
    fun getRecent(){
        model.getRecentContent {
            listRecent.postValue(it)
        }
    }
    fun getDatabaseSize(){
        model.getSizeDatabase {
            dbSize.postValue(it)
        }
    }

}