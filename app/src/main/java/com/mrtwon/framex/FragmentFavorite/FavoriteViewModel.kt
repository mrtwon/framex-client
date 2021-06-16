package com.mrtwon.framex.FragmentFavorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.startandroid.MyApplication
import com.mrtwon.framex.Model.ModelDatabase
import com.mrtwon.framex.room.Content
import com.mrtwon.framex.room.Favorite

class FavoriteViewModel: ViewModel() {
    val model = ModelDatabase()
    val dao = MyApplication.getInstance.DB.dao()
    val favoriteLiveData: LiveData<List<Favorite>> = dao.getFavoriteLiveData()
    val contentList = MutableLiveData<List<Content>>()
    fun getContent(){
        model.getFavorite {
            contentList.postValue(it)
        }
    }
    fun removeFavorite(id: Int, contentType: String){
        model.removeFavorite(id, contentType)
    }
}







