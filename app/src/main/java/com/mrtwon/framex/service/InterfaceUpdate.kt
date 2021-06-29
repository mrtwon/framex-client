package com.mrtwon.framex.service

import com.mrtwon.framex.Retrofit.InstanceApi
import com.mrtwon.framex.Retrofit.Kinopoisk.POJOKinopoisk
import com.mrtwon.framex.Retrofit.KinopoiskRating.RatingApi
import com.mrtwon.framex.Retrofit.KinopoiskRating.RatingPOJO
import kotlinx.coroutines.*

interface InterfaceUpdate<T> {

    suspend fun updateDatabase(callUpdate: (Unit) -> Unit): Boolean

    suspend fun forUpdate(): List<T>

    fun isExisting(item: T): Boolean

    suspend fun giveKp(kp_id: Int): Deferred<POJOKinopoisk?> = GlobalScope.async(Dispatchers.IO) {
        InstanceApi.kinopoisk.filmsInfo(kp_id).execute().body()
    }

    suspend fun giveRating(kp_id: Int): Deferred<RatingPOJO> = GlobalScope.async(Dispatchers.IO){
        RatingApi.getRating(kp_id)
    }

}