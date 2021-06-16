package com.mrtwon.framex

import android.view.Display
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtwon.framex.Content.GenresEnum
import com.mrtwon.framex.Model.ModelDatabase
import com.mrtwon.framex.room.Content
import com.mrtwon.framex.room.DatabaseSize

class MainViewModel: ViewModel() {
    val model = ModelDatabase()
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