package com.mrtwon.framex.Modules

import com.mrtwon.framex.Model.Model
import dagger.Module
import dagger.Provides

@Module
class ModuleModel {
   @Provides
   fun getModel(): Model = Model()
}