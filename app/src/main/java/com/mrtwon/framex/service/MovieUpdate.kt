package com.mrtwon.framex.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.startandroid.MyApplication
import com.mrtwon.framex.Model.Model
import com.mrtwon.framex.Retrofit.InstanceApi
import com.mrtwon.framex.Retrofit.VideoCdn.Movies.DataItem
import com.mrtwon.framex.room.*
import kotlinx.coroutines.*

class MovieUpdate : InterfaceUpdate<DataItem> {
    private val CONTENT_TYPE = "movie"
    private val model: Model = MyApplication.getInstance.appComponents.getModel()
    private val contentForUpdate = arrayListOf<DataItem>()
    var contentForProgress = hashSetOf<DataItem>()
    val progressIntUpdate = MutableLiveData<Int>()
    lateinit var scope: CoroutineScope

    override suspend fun updateDatabase(callUpdate: (Unit) -> Unit): Boolean {
        return scope.async {
            val listContent = when {
                contentForUpdate.isEmpty() -> forUpdate()
                else -> ArrayList<DataItem>(contentForUpdate)
            }
            log("START UPDATE MOVIE LIST SIZE = ${listContent.size}")
            for (element in listContent) {
                element.kinopoiskId?.let {
                    ensureActive()
                    addDatabase(element)
                    callUpdate(Unit)
                    contentForUpdate.remove(element)
                }
            }
            log("end for movie update")
            true
        }.await()
    }

    override suspend fun forUpdate(): List<DataItem> {
        return scope.async {
            val api = InstanceApi.videoCdn
            var currentPage = 1
            ensureActive()
            val totalPage = api.nextPageMovie(currentPage).execute().body()?.lastPage!!
            val result = arrayListOf<DataItem>()
            var isActual = true
            while (isActual && totalPage >= currentPage) {
                ensureActive()
                val contentList = api.nextPageMovie(currentPage).execute().body()!!.data!!
                val actualStatus = pageProcessing(contentList, result)
                isActual = actualStatus
                currentPage++
            }
            result.reverse()
            contentForUpdate.addAll(result)
            log("[METKA MOVIE] result size ${result.size} | contentForUpdate size ${contentForUpdate.size}")
            contentForUpdate
        }.await()
    }
    suspend fun pageProcessing(list_page: List<DataItem?>, resultList: ArrayList<DataItem>): Boolean{
        for (element in list_page) {
            if (element?.kinopoiskId != null && isExistingList(element)) {
                if (isExisting(element)) {
                    log("[movie]  add new element [id ${element.kinopoiskId}]")
                    resultList.add(element)
                    addElementForProgressSet(element)
                } else {
                    log("[movie]  exit for circle. [id ${element.kinopoiskId}]")
                    return false
                }
            }
        }
        return true
    }

    suspend fun addDatabase(cdn: DataItem){
        cdn.kinopoiskId?.let {
            val kp_pojo = giveKp(it.toInt()).await()
            val rating = giveRating(it.toInt()).await()
            // result object for add to database
            val serial = Movie.build(rating, kp_pojo, cdn)
            val genres = GenresMovie.build(kp_pojo, cdn)
            val countries = CountriesMovie.build(kp_pojo, cdn)
            //add to database
            model.addMovieSync(serial)
            model.addGenresSync(genres, CONTENT_TYPE)
            model.addCountriesSync(countries, CONTENT_TYPE)
        }
    }

    private fun addElementForProgressSet(element: DataItem){
        if(!contentForProgress.contains(element)) {
            contentForProgress.add(element)
            progressIntUpdate.postValue(contentForProgress.size)
        }
    }
    override fun isExisting(item: DataItem): Boolean {
        return model.db.dao().movieIsAlreadyById(item.id!!) == null
    }
    fun isExistingList(item: DataItem): Boolean {
        val result = contentForUpdate.count { dataItem: DataItem -> dataItem.id == item.id } == 0
        return result
    }
    fun log(msg: String){
        Log.i("self-service",msg)
    }
}
