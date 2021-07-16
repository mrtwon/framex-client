package com.mrtwon.framex.FragmentFavorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.startandroid.MyApplication
import com.mrtwon.framex.GeneralVM
import com.mrtwon.framex.room.Content
import com.mrtwon.framex.room.Favorite

class FavoriteViewModel: GeneralVM() {


    val dao = MyApplication.getInstance.DB.dao()
    val favoriteLiveData: LiveData<List<Favorite>> = dao.getFavoriteLiveData()
    val contentList = MutableLiveData<List<Content>>()

    fun getContent(){
        Log.i("self-favorite","getContent()")
        model.getFavorite {
            contentList.postValue(it)
        }
    }

    fun removeFavorite(id: Int, contentType: String){
        model.removeFavorite(id, contentType)
    }

}







