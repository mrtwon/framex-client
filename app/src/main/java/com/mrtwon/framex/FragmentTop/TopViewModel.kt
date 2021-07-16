package com.mrtwon.framex.FragmentTop

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex.Content.CollectionContentEnum
import com.mrtwon.framex.Content.ContentTypeEnum
import com.mrtwon.framex.Content.GenresEnum
import com.mrtwon.framex.GeneralVM
import com.mrtwon.framex.room.Content
import kotlinx.coroutines.DelicateCoroutinesApi

class TopViewModel: GeneralVM() {

    val listLiveData =  MutableLiveData<List<Content>>()
    @DelicateCoroutinesApi
    fun getContentByGenresEnum(genres: GenresEnum, content: ContentTypeEnum){
        model.getTopByGenresEnum(genres, content){
            listLiveData.postValue(it)
        }
    }
    @DelicateCoroutinesApi
    fun getContentByCollectionEnum(collection: CollectionContentEnum, content: ContentTypeEnum){
        model.getTopByCollectionEnum(collection, content){
            listLiveData.postValue(it)
        }
    }
}