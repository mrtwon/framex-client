
package com.mrtwon.framex.Modules

import com.example.testbook.Retrofit.Kinopoisk.KinopoiskApi
import com.github.mrtwon.library.IgnoreCode
import com.github.mrtwon.library.XmlParse
import com.github.mrtwon.library.XmlParseBuilder
import com.mrtwon.framex.Retrofit.InstanceApi
import com.mrtwon.framex.Retrofit.VideoCdn.VideoCdnApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule {
    @Singleton
    @Provides
    fun getRatingApi(client: OkHttpClient): XmlParse{
        return XmlParseBuilder()
            .addBaseUrl("https://rating.kinopoisk.ru/")
            .addExpansion(true)
            .addIgnoreList(arrayListOf(IgnoreCode.NOT_FOUND))
            .addOkHttpClient(client)
            .build()
    }
    @Singleton
    @Provides
    fun getVideoCdnApi(client: OkHttpClient): VideoCdnApi{
        return Retrofit.Builder()
            .baseUrl("https://videocdn.tv/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(VideoCdnApi::class.java)
    }
    @Singleton
    @Provides
    fun getKinopoiskApi(client: OkHttpClient): KinopoiskApi{
        return Retrofit.Builder()
            .baseUrl("https://kinopoiskapiunofficial.tech/api/v2.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(KinopoiskApi::class.java)
    }
    @Singleton
    @Provides
    fun getOkHttpClient(): OkHttpClient{
        return OkHttpClient().newBuilder()
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .build()
    }
}
