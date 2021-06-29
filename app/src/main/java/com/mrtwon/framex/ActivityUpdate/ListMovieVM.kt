package com.mrtwon.framex.ActivityUpdate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtwon.framex.Model.ModelApi
import com.mrtwon.framex.room.Movie

class ListMovieVM: ViewModel() {
    val modelApi = ModelApi()
    val movieList = MutableLiveData<List<Movie>>()
    val visibilityPb = MutableLiveData<Boolean>()
    val noConnect = MutableLiveData<Boolean>()
    fun searchMovie(query: String){
        visibilityPb.postValue(true)
        modelApi.searchMovie(query, {
            visibilityPb.postValue(false)
            movieList.postValue(it)
        },{
            noConnect.postValue(it)
        })
    }
}