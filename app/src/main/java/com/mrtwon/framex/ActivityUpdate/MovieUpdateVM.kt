package com.mrtwon.framex.ActivityUpdate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtwon.framex.Model.ModelApi
import com.mrtwon.framex.room.Movie
import com.mrtwon.framex.room.Serial

class MovieUpdateVM: ViewModel() {
    val model = ModelApi()
    val listMovie = MutableLiveData<List<Movie>>()
    fun search(query: String){
        model.searchMovie(query){
            listMovie.postValue(it)
        }
    }
}