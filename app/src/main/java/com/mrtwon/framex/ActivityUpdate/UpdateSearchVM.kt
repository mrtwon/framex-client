package com.mrtwon.framex.ActivityUpdate

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex.GeneralVM
import com.mrtwon.framex.room.Movie
import com.mrtwon.framex.room.Serial

class UpdateSearchVM: GeneralVM() {

    val movieToUpdate = MutableLiveData<ArrayList<Movie>>() // for update
    val serialToUpdate = MutableLiveData<ArrayList<Serial>>() //for update
    val searchQuery = MutableLiveData<String>() // LiveData with search request
    val toastLiveData = MutableLiveData<String>() // toast for notify user

    fun update(){
        val sizeMovie = movieToUpdate.value?.size ?: 0
        val sizeSerial = serialToUpdate.value?.size ?: 0
        if(sizeMovie == 0 && sizeSerial == 0){
            toastLiveData.postValue("Вы еще ничего не выбрали")
        }else{
            model.createNewContentFromMix(serialToUpdate.value, movieToUpdate.value){
                if(it){
                    movieToUpdate.postValue(arrayListOf())
                    serialToUpdate.postValue(arrayListOf())
                    toastLiveData.postValue("Успешно")
                }else{
                    toastLiveData.postValue("Ошибка запроса")
                }
            }
        }
    }
}