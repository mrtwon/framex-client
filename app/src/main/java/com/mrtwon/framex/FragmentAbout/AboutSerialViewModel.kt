package com.mrtwon.framex.FragmentAbout

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex.GeneralVM
import com.mrtwon.framex.room.SerialWithGenres
import kotlinx.coroutines.DelicateCoroutinesApi

class AboutSerialViewModel: GeneralVM() {

    val contentData = MutableLiveData<SerialWithGenres>()
    val isFavoriteBoolean = MutableLiveData<Boolean>()
    @DelicateCoroutinesApi
    fun getAbout(id: Int){
        model.getAboutSerial(id){
            contentData.postValue(it)
        }
    }
    fun addFavorite(id: Int, contentType: String = "tv_series"){
        model.addFavorite(id, contentType)
    }
    fun deleteFavorite(id: Int, contentType: String = "tv_series"){
        model.removeFavorite(id, contentType)
    }
    fun isFavorite(id: Int, contentType: String = "tv_series"){
        model.isFavorite(id, contentType){
            isFavoriteBoolean.postValue(it)
        }
    }
}