package com.mrtwon.framex.Model

import com.mrtwon.framex.Retrofit.InstanceApi
import com.mrtwon.framex.Retrofit.Kinopoisk.POJOKinopoisk
import com.mrtwon.framex.Retrofit.KinopoiskRating.RatingApi
import com.mrtwon.framex.Retrofit.KinopoiskRating.RatingPOJO
import com.mrtwon.framex.room.Movie
import com.mrtwon.framex.room.Serial
import kotlinx.coroutines.*

class ModelApi {
    private val modelDB = ModelDatabase()
    fun searchSerial(query: String, callback: (List<Serial>) -> Unit){
        GlobalScope.launch {
            val responseListSerials =
                InstanceApi.videoCdn.searchSerial(query).execute().body()?.data
            val result = arrayListOf<Serial>()
            if (responseListSerials != null) {
                for (element in responseListSerials) {
                    if (element?.id != null && element.contentType != null) {
                        if (!modelDB.isExistingSync(element.id, element.contentType)) {
                            continue
                        }
                    } else { continue }
                    element.kinopoiskId?.let {
                        val kp_pojo = InstanceApi.kinopoisk.filmsInfo(it.toInt()).execute().body()
                        val rating = RatingApi.getRating(it.toInt())
                        result.add(
                            Serial.build(
                                rating, kp_pojo, element
                            )
                        )
                    }
                }
                callback(result)
            }
        }
    }
    fun searchMovie(query: String,callback: (List<Movie>) -> Unit) {
        GlobalScope.launch {
            val responseListSerials = InstanceApi.videoCdn.searchMovie(query).execute().body()?.data
            val result = arrayListOf<Movie>()
            if (responseListSerials != null) {
                for (element in responseListSerials) {
                    if (element?.id != null && element.contentType != null) {
                        if (!modelDB.isExistingSync(element.id, element.contentType)) {
                            continue
                        }
                    } else {
                        continue
                    }
                    element.kinopoiskId?.let {
                        val kp_pojo = InstanceApi.kinopoisk.filmsInfo(it.toInt()).execute().body()
                        val rating = RatingApi.getRating(it.toInt())
                        result.add(
                            Movie.build(
                                rating, kp_pojo, element
                            )
                        )
                    }
                }
                callback(result)
            }
        }
    }


    fun giveKpSync(kp_id: Int): POJOKinopoisk? = InstanceApi.kinopoisk.filmsInfo(kp_id).execute().body()
    fun giveRatingSync(kp_id: Int): RatingPOJO = RatingApi.getRating(kp_id)
}