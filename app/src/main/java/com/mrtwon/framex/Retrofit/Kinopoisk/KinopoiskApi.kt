package com.example.testbook.Retrofit.Kinopoisk


import com.mrtwon.framex.Retrofit.Kinopoisk.POJOKinopoisk
import retrofit2.Call
import retrofit2.http.*

interface KinopoiskApi {
    @Headers("X-API-KEY: 65f45586-9abe-407f-8a5b-03c0710a4b8a",
        "Content-Type: application/json")
    @GET("films/{id}")
    fun filmsInfo(@Path("id") id: Int): Call<POJOKinopoisk>
}