package com.mrtwon.framex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.startandroid.MyApplication
import com.mrtwon.framex.Model.ModelDatabase
import com.mrtwon.framex.room.Favorite

class TestViewModel: ViewModel() {
    val model = ModelDatabase()
    val db = MyApplication.getInstance.DB.dao()
    val liveData: LiveData<List<Favorite>> = db.getFavoriteLiveData()
    fun add(id: Int, contentType: String = "movie"){
        model.addFavorite(id, contentType)
    }
    fun remove(id: Int, contentType: String = "movie"){
        model.removeFavorite(id, contentType)
    }
}