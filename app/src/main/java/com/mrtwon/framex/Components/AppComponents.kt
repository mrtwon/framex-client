package com.mrtwon.framex.Components

import com.mrtwon.framex.GeneralVM
import com.mrtwon.framex.Model.Model
import com.mrtwon.framex.Modules.ModuleModel
import dagger.Component

@Component(modules = [ModuleModel::class])
abstract class AppComponents {
    abstract fun inject(vm: GeneralVM)
    abstract fun getModel(): Model
}