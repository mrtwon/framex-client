package com.mrtwon.framex.FragmentAbout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtwon.framex.Model.ModelDatabase
import com.mrtwon.framex.room.MovieWithGenres
import kotlinx.coroutines.DelicateCoroutinesApi

class AboutMovieViewModel: ViewModel() {
    val model = ModelDatabase()
    val contentData = MutableLiveData<MovieWithGenres>()
    val isFavoriteBoolean = MutableLiveData<Boolean>()
    @DelicateCoroutinesApi
    fun getAbout(id: Int){
        model.getAboutMovie(id){
            contentData.postValue(it)
        }
    }
    fun addFavorite(id: Int, contentType: String = "movie"){
        model.addFavorite(id, contentType)
    }
    fun deleteFavorite(id: Int, contentType: String = "movie"){
        model.removeFavorite(id, contentType)
    }
    fun isFavorite(id: Int, contentType: String = "movie"){
        model.isFavorite(id, contentType){
            isFavoriteBoolean.postValue(it)
        }
    }
}