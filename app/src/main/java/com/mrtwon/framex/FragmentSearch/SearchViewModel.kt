package com.mrtwon.framex.FragmentSearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtwon.framex.Model.ModelDatabase
import com.mrtwon.framex.room.Content

class SearchViewModel: ViewModel() {
    val model = ModelDatabase()
    val searchContent = MutableLiveData<List<Content>>()

    fun search(searchString: String){
        model.getSearchResult(searchString){
           searchContent.postValue(it)
        }
    }
    fun searchDescription(searchString: String){
        model.searchDescription(searchString){
            searchContent.postValue(it)
        }
    }
}