package com.mrtwon.framex.service

import android.util.Log
import com.example.startandroid.MyApplication
import com.mrtwon.framex.Retrofit.InstanceApi
import com.mrtwon.framex.Retrofit.VideoCdn.Movies.DataItem
import com.mrtwon.framex.room.Movie
import kotlinx.coroutines.*

class MovieUpdate : InterfaceUpdate<DataItem> {
    private val contentForUpdate = arrayListOf<DataItem>()
    var contentForProgress = hashSetOf<DataItem>()
    lateinit var scope: CoroutineScope

    override suspend fun updateDatabase(callUpdate: (Unit) -> Unit): Boolean {
        return scope.async {
            val db = MyApplication.getInstance.DB.dao()
            val listContent = when {
                contentForUpdate.isEmpty() -> forUpdate()
                else -> ArrayList<DataItem>(contentForUpdate)
            }
            log("START UPDATE MOVIE LIST SIZE = ${listContent.size}")
            for (element in listContent) {
                element.kinopoiskId?.toInt()?.apply {
                    val movie = Movie.build(
                        giveRating(this).await(),
                        giveKp(this).await(),
                        element
                    )
                    ensureActive()
                    db.addMovie(movie)
                    callUpdate(Unit)
                    contentForUpdate.remove(element)
                    log("[updateDB Movie] $movie")
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
            val totalPage = api.nextPageMovie(currentPage).execute().body()?.lastPage!!
            ensureActive()
            val result = arrayListOf<DataItem>()
            var isActual = true
            while (isActual && totalPage >= currentPage) {
                val contentList = api.nextPageMovie(currentPage).execute().body()!!.data!!
                ensureActive()
                for (element in contentList) {
                    if (element?.kinopoiskId != null && isExistingList(element)) {
                        if (isExisting(element)) {
                            log("[movie]  add new element [id ${element.kinopoiskId}]")
                            result.add(element)
                            contentForProgress.add(element)
                        } else {
                            log("[movie]  exit for circle. [id ${element.kinopoiskId}]")
                            isActual = false
                            break
                        }
                    }
                }
                currentPage++
            }
            result.reverse()
            contentForUpdate.addAll(result)
            log("[METKA MOVIE] result size ${result.size} | contentForUpdate size ${contentForUpdate.size}")
            contentForUpdate
        }.await()
    }

    override fun isExisting(item: DataItem): Boolean {
        val db = MyApplication.getInstance.DB.dao()
        return db.movieIsAlreadyById(item.id!!) == null
    }
    fun isExistingList(item: DataItem): Boolean {
        val result = contentForUpdate.count { dataItem: DataItem -> dataItem.id == item.id } == 0
        return result
    }
    fun log(msg: String){
        Log.i("self-service",msg)
    }
}
