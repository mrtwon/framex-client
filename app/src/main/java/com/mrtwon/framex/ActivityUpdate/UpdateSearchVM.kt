package com.mrtwon.framex.ActivityUpdate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtwon.framex.Model.ModelApi
import com.mrtwon.framex.Model.ModelDatabase
import com.mrtwon.framex.room.Movie
import com.mrtwon.framex.room.Serial

class UpdateSearchVM: ViewModel() {
    private val model = ModelDatabase()
    private val modeApi = ModelApi()

    val movieToUpdate = MutableLiveData<ArrayList<Movie>>() // for update
    val serialToUpdate = MutableLiveData<ArrayList<Serial>>() //for update

    val movieList = MutableLiveData<List<Movie>>() // search result
    val serialList = MutableLiveData<List<Serial>>() // search result

    val searchQuery = MutableLiveData<String>() // LiveData with search request
    val toastLiveData = MutableLiveData<String>() // toast for notify user
    val visibilityPb = MutableLiveData<Boolean>()
    fun search(query: String){
        visibilityPb.postValue(true)
        modeApi.searchMovie(query){
            visibilityPb.postValue(false)
            movieList.postValue(it)
        }
        modeApi.searchSerial(query){
            visibilityPb.postValue(false)
            serialList.postValue(it)
        }
    }
    fun update(){
        val sizeMovie = movieToUpdate.value?.size ?: 0
        val sizeSerial = serialToUpdate.value?.size ?: 0
        if(sizeMovie == 0 && sizeSerial == 0){
            toastLiveData.postValue("Вы еще ничего не выбрали")
        }else{
            movieToUpdate.value?.let {
                model.createdNewContents(it, "movie")
            }
            serialToUpdate.value?.let {
                model.createdNewContents(it, "tv_series")
            }
            movieToUpdate.postValue(arrayListOf())
            serialToUpdate.postValue(arrayListOf())
            toastLiveData.postValue("Успешно")
        }
    }
}