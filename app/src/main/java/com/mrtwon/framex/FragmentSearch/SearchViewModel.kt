package com.mrtwon.framex.FragmentSearch

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex.GeneralVM
import com.mrtwon.framex.room.Content

class SearchViewModel: GeneralVM() {
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