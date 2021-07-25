package com.mrtwon.framex.Components

import com.example.testbook.Retrofit.Kinopoisk.KinopoiskApi
import com.github.mrtwon.library.XmlParse
import com.mrtwon.framex.GeneralVM
import com.mrtwon.framex.Model.Model
import com.mrtwon.framex.Modules.ApiModule
import com.mrtwon.framex.Modules.ModuleModel
import com.mrtwon.framex.Retrofit.VideoCdn.VideoCdnApi
import com.mrtwon.framex.room.Database
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ModuleModel::class, ApiModule::class])
abstract class AppComponents {
    abstract fun inject(vm: GeneralVM)

    abstract fun getModel(): Model

    abstract fun getDatabase(): Database

    abstract fun getRatingApi(): XmlParse

    abstract fun getVideoCdnApi(): VideoCdnApi

    abstract fun getKinopiskApi(): KinopoiskApi
}