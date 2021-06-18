package com.mrtwon.framex.service

import android.util.Log
import android.widget.Toast
import com.example.startandroid.MyApplication
import com.mrtwon.framex.Retrofit.InstanceApi
import com.mrtwon.framex.Retrofit.VideoCdn.TvSeries.DataItem
import com.mrtwon.framex.room.Serial
import kotlinx.coroutines.*
import java.util.*


class SerialUpdate() : InterfaceUpdate<DataItem>  {
    private val contentForUpdate = arrayListOf<DataItem>()
    var contentForProgress = hashSetOf<DataItem>()
    lateinit var scope: CoroutineScope
    override suspend fun forUpdate(): List<DataItem> {
        return scope.async {
            log("start size = ${contentForUpdate.size}")
            val result = arrayListOf<DataItem>()
            val api = InstanceApi.videoCdn
            var currentPage = 1
            val totalPage = api.nextPageSerial(currentPage).execute().body()?.lastPage!!
            ensureActive()
            log("start 2")
            var isActual = true
            while (isActual && totalPage >= currentPage) {
                val contentList = api.nextPageSerial(currentPage).execute().body()!!.data!!
                ensureActive()
                log("start 3")
                for (element in contentList) {
                    if (element?.kinopoiskId != null && isExistingList(element)) {
                        if (isExisting(element)) {
                            log("[serial]   add new element [id ${element.kinopoiskId}]")
                            result.add(element)
                            contentForProgress.add(element)
                        } else {
                            log("[serial]  exit for circle. [id ${element.kinopoiskId}]")
                            isActual = false
                            break
                        }
                    }
                }
                currentPage++
            }
            result.reverse()
            contentForUpdate.addAll(result)
            log("[METKA SERIAL] result size ${result.size} | contentForUpdate size ${contentForUpdate.size}")
            contentForUpdate
        }.await()
    }

    override suspend fun updateDatabase(callUpdate: (Unit) -> Unit): Boolean {

        return scope.async {
            val db = MyApplication.getInstance.DB.dao()
            val listContent = when {
                contentForUpdate.isEmpty() -> forUpdate()
                else -> ArrayList<DataItem>(contentForUpdate)
            }
            log("START UPDATE SERIAL LIST SIZE = ${listContent.size}")
            var i = 0
            while (listContent.size > i) {
                listContent[i].kinopoiskId?.toInt()?.apply {
                    ensureActive()
                    val serial = Serial.build(
                        giveRating(this).await(),
                        giveKp(this).await(),
                        listContent[i]
                    )
                    ensureActive()
                    db.addSerial(serial)
                    callUpdate(Unit)
                    contentForUpdate.remove(listContent[i])
                    log("[$isActive][updateDB Serial] $serial")
                }
                i++
            }
            log("end for serial update")
            true
        }.await()
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

    override fun isExisting(item: DataItem): Boolean {
        val db = MyApplication.getInstance.DB.dao()
        return db.serialIsAlreadyById(item.id!!) == null
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