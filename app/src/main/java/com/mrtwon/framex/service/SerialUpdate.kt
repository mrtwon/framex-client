package com.mrtwon.framex.service

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.startandroid.MyApplication
import com.mrtwon.framex.Model.Model
import com.mrtwon.framex.Retrofit.InstanceApi
import com.mrtwon.framex.Retrofit.VideoCdn.TvSeries.DataItem
import com.mrtwon.framex.room.Countries
import com.mrtwon.framex.room.Genres
import com.mrtwon.framex.room.Serial
import kotlinx.coroutines.*
import java.util.*


class SerialUpdate() : InterfaceUpdate<DataItem>  {
    private val CONTENT_TYPE = "tv_series"
    private val model: Model = MyApplication.getInstance.appComponents.getModel()
    private val contentForUpdate = arrayListOf<DataItem>()
    val contentForProgress = hashSetOf<DataItem>()
    val progressIntUpdate = MutableLiveData<Int>()
    //val contentForProgress = MutableLiveData<HashSet<DataItem>>()
    lateinit var scope: CoroutineScope

    override suspend fun updateDatabase(callUpdate: (Unit) -> Unit): Boolean {
        return scope.async {
            val listContent = when {
                contentForUpdate.isEmpty() -> forUpdate()
                else -> ArrayList<DataItem>(contentForUpdate)
            }
            log("START UPDATE SERIAL LIST SIZE = ${listContent.size}")

            for(elem in listContent) {
                elem.kinopoiskId?.let {
                    ensureActive()
                    addDatabase(elem)
                    callUpdate(Unit)
                    contentForUpdate.remove(elem)
                }
            }
            log("end for serial update")
            true
        }.await()
    }

    override suspend fun forUpdate(): List<DataItem> {
        return scope.async {
            val result = arrayListOf<DataItem>()
            val api = InstanceApi.videoCdn
            var currentPage = 1
            val totalPage = api.nextPageSerial(currentPage).execute().body()?.lastPage!!
            ensureActive()
            var isActual = true
            while (isActual && totalPage >= currentPage) {
                ensureActive()
                val contentList = api.nextPageSerial(currentPage).execute().body()!!.data!!
                val actualStatus = pageProcessing(contentList, result)
                isActual = actualStatus
                currentPage++
            }
            result.reverse()
            contentForUpdate.addAll(result)
            contentForUpdate
        }.await()
    }

    suspend fun pageProcessing(list_page: List<DataItem?>, resultList: ArrayList<DataItem>): Boolean{
        for (element in list_page) {
            if (element?.kinopoiskId != null && isExistingList(element)) {
                if (isExisting(element)) {
                    log("[serial]  add new element [id ${element.kinopoiskId}]")
                    resultList.add(element)
                    addElementForProgressSet(element)
                } else {
                    log("[serial]  exit for circle. [id ${element.kinopoiskId}]")
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
            val serial = Serial.build(rating, kp_pojo, cdn)
            val genres = Genres.build(kp_pojo, cdn)
            val countries = Countries.build(kp_pojo, cdn)
            //add to database
            model.addSerialSync(serial)
            model.addGenresSync(genres, CONTENT_TYPE)
            model.addCountriesSync(countries, CONTENT_TYPE)
        }
    }

    private fun notifyIsConnect(status: Boolean) {
        //serial - 9671 (328)
        //movie 50321 (54)
        val msg = when (status) {
            true -> "Работаем дальше!"
            false -> "Ожидание сети..."
        }
        log(msg)
        scope.launch(Dispatchers.Main) {
            Toast.makeText(MyApplication.getInstance.applicationContext, msg, Toast.LENGTH_SHORT)
                .show()
        }
    }
    private fun addElementForProgressSet(element: DataItem){
        if(!contentForProgress.contains(element)) {
            contentForProgress.add(element)
            progressIntUpdate.postValue(contentForProgress.size)
        }
    }
    override fun isExisting(item: DataItem): Boolean {
        return model.db.dao().serialIsAlreadyById(item.id!!) == null
    }
    fun isExistingList(item: DataItem): Boolean {
        val result = contentForUpdate.count { dataItem: DataItem -> dataItem.id == item.id } == 0
        if(!result){
            log("element already exist")
        }
        else{
            log("element is not existing[ id = ${item.id}]")
        }
        return result
    }
    fun log(msg: String){
        Log.i("self-service",msg)
    }
}