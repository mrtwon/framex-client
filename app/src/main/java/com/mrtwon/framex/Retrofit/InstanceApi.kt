package com.mrtwon.framex.Retrofit
import com.example.testbook.Retrofit.Kinopoisk.KinopoiskApi
import com.mrtwon.framex.Retrofit.VideoCdn.VideoCdnApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object InstanceApi{
    val okHttp = OkHttpClient.Builder()
        .readTimeout(2, TimeUnit.MINUTES)
        .connectTimeout(2, TimeUnit.MINUTES)
        .build()
    val videoCdn: VideoCdnApi by lazy{
        Retrofit.Builder()
            .baseUrl("https://videocdn.tv/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
            .create(VideoCdnApi::class.java)
    }

    val kinopoisk: KinopoiskApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://kinopoiskapiunofficial.tech/api/v2.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
            .create(KinopoiskApi::class.java)
    }
}