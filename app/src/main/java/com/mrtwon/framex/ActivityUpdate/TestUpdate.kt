/*
package com.mrtwon.framex.ActivityUpdate

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.startandroid.MyApplication
import com.mrtwon.framex.R
import com.mrtwon.framex.Retrofit.InstanceApi
import com.mrtwon.framex.Retrofit.Kinopoisk.POJOKinopoisk
import com.mrtwon.framex.Retrofit.KinopoiskRating.RatingApi
import com.mrtwon.framex.Retrofit.KinopoiskRating.RatingPOJO
import com.mrtwon.framex.Retrofit.VideoCdn.TvSeries.DataItem
import com.mrtwon.framex.room.*
import kotlinx.coroutines.*

class TestUpdate: AppCompatActivity() {
    val api = InstanceApi.videoCdn
    lateinit var go: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.test_update)
        go = findViewById(R.id.go)
        go.setOnClickListener{
            stepThree()
        }
        super.onCreate(savedInstanceState)
    }
    fun <T : Genres> helperConvertGenres(list: List<T>): String{
        var result = ""
        for(elem in list){
            result += "${elem.genres} "
        }
        return result
    }
    fun <T : Countries> helperConvertCountries(list: List<T>): String{
        var result = ""
        for(elem in list){
            result += "${elem.country} "
        }
        return result
    }

    fun stepThree() {
        GlobalScope.launch {
            //stepThreeGenresSerial()
            //stepThreeGenresMovie()
            //stepThreeCountriesMovie()
            stepThreeCountriesSerial()
        }
    }
    fun stepThreeGenresMovie(){
        GlobalScope.launch {
            */
/*
            val db = MyApplication.getInstance.DB.dao()
            val listId = db.stepThreeGenresForUpdatingSerial()
            log("[?] start updating. List size = ${listId.size}\n\n")
            for (element in listId) {
                val pojo_kp = giveKp(element.kp_id!!).await()
                val genres = Genres.buildOther(pojo_kp, element.kp_id, element.imdb_id)
                val countries = Countries.buildOther(pojo_kp, element.kp_id, element.imdb_id)
                val genresString = helperConvertGenres(genres)
                val countriesString = helperConvertCountries(countries)
                db.addGenresSerial(genres)
                db.addCountriesSerial(countries)
                log("[+] updating [id ${element.id}|${element.kp_id}|${element.imdb_id}] genres: $genresString  |  countries: $countriesString")
            }*//*

            val db = MyApplication.getInstance.DB.dao()
            val listId = db.stepThreeGenresForUpdatingMovie()
            log("[?] start updating. List size = ${listId.size}\n\n")
            for(element in listId){
                val pojo_kp = giveKp(element.kp_id!!).await()
                val genres = GenresMovie.buildOther(pojo_kp, element.kp_id, element.imdb_id)
                db.addGenresMovie(genres)
                val genresString = helperConvertGenres(genres)
                log("[+] updating [id ${element.id}|${element.kp_id}|${element.imdb_id}] genres: $genresString")
            }
        }
    }
    fun stepThreeGenresSerial(){
        GlobalScope.launch {
            val db = MyApplication.getInstance.DB.dao()
            val listId = db.stepThreeGenresForUpdatingSerial()
            log("[?] start updating. List size = ${listId.size}\n\n")
            for (element in listId) {
                val pojo_kp = giveKp(element.kp_id!!).await()
                val genres = Genres.buildOther(pojo_kp, element.kp_id, element.imdb_id)
                val genresString = helperConvertGenres(genres)
                db.addGenresSerial(genres)
                log("[+] updating [id ${element.id}|${element.kp_id}|${element.imdb_id}] genres: $genresString")
            }
        }
    }
    fun stepThreeCountriesMovie() {
        GlobalScope.launch {
            val db = MyApplication.getInstance.DB.dao()
            val listId = db.stepThreeCountriesForUpdateMovie()
            log("[?] start updating. List size = ${listId.size}\n\n")
            for (element in listId) {
                val pojo_kp = giveKp(element.kp_id!!).await()
                val countries = CountriesMovie.buildOther(pojo_kp, element.kp_id, element.imdb_id)
                //db.addGenresMovie(genres)
                db.addCountriesMovie(countries)
                //val genresString = helperConvertGenres(genres)
                val countriesString = helperConvertCountries(countries)
                log("[+] updating [id ${element.id}|${element.kp_id}|${element.imdb_id}] countries: $countriesString")
            }
        }
    }
    fun stepThreeCountriesSerial() {
        GlobalScope.launch {
            val db = MyApplication.getInstance.DB.dao()
            val listId = db.stepThreeCountriesForUpdateSerial()
            log("[?] start updating. List size = ${listId.size}\n\n")
            for (element in listId) {
                val pojo_kp = giveKp(element.kp_id!!).await()
                val countries = Countries.buildOther(pojo_kp, element.kp_id, element.imdb_id)
                //db.addGenresMovie(genres)
                db.addCountriesSerial(countries)
                //val genresString = helperConvertGenres(genres)
                val countriesString = helperConvertCountries(countries)
                log("[+] updating [id ${element.id}|${element.kp_id}|${element.imdb_id}] countries: $countriesString")
            }
        }
    }
    fun stepOne(){
        GlobalScope.launch {
            */
/*val db = MyApplication.getInstance.DB.dao()
            val idList = db.stepOneSerial()
            for(element in idList){
                val listFromTwoElement = db.stepOneGetSerial(element.kp_id!!)
                for(two_element in listFromTwoElement){
                    val idString = getIdByString(two_element.iframe_src!!)
                    if(two_element.id != idString){
                        db.stepOneRemoveByIdSerial(two_element.id)
                        log("remove [${two_element.id} != $idString]")
                    }else{
                        log("else ... [${two_element.id} == $idString]")
                    }
                }
            }*//*

            val db = MyApplication.getInstance.DB.dao()
            val idList = db.stepOneMovie()
            for(element in idList){
                val listFromTwoElement = db.stepOneGetMovie(element.kp_id!!)
                for(two_element in listFromTwoElement){
                    val idString = getIdByString(two_element.iframe_src!!)
                    if(two_element.id != idString){
                        db.stepOneRemoveByIdMovie(two_element.id)
                        log("remove [${two_element.id} != $idString]")
                    }else{
                        log("else ... [${two_element.id} == $idString]")
                    }
                }
            }
        }
    }
    fun reIndex(){
        GlobalScope.launch {
            val db = MyApplication.getInstance.DB.dao()
            val allSerial = db.stepTwoGetAllMovie()
            var indexChanged = 100000
            for (element in allSerial) {
                db.stepOneUpdateByIdMovie(element.id, indexChanged)
                indexChanged++
                log("old id ${element.id} | new id $indexChanged")
            }
        }
    }
    fun stepTwo(){
        GlobalScope.launch {
            val db = MyApplication.getInstance.DB.dao()
            val idList = db.stepTwoGetAllMovie()
            for(element in idList){
                val idString = getIdByString(element.iframe_src!!)
                if(element.id != idString){
                    log("replace: ${element.id} -> $idString")
                    db.stepOneUpdateByIdMovie(element.id, idString)
                }else{
                    log("ok. ${element.id} = $idString")
                }
            }
        }
    }
    fun updateSerial(){
        GlobalScope.launch {
            val type = "tv_series"
            var currentPage = 2
            val totalPage = api.nextPageMovie(currentPage).execute().body()?.lastPage!!
            log("last page = $totalPage")
            while (totalPage >= currentPage) {
                val contentList = api.nextPageSerial(currentPage).execute().body()?.data
                if (contentList != null) {
                    for (element in contentList) {
                        if(element != null){
                            addDatabase(element, type)
                        }
                    }
                }
                currentPage++
            }
        }
    }
    fun getIdByString(s: String): Int{
        var result = StringBuilder()
        val char_array = s.toCharArray()
        char_array.reverse()
        for(char_element in char_array){
            if(char_element == '/'){
                break
            }
            result.append(char_element)
        }
        return (result.reverse()).toString().toInt()
    }
    suspend fun addDatabase(cdn: DataItem, CONTENT_TYPE: String){
        val modelDatabase = ModelDatabase()
        cdn.kinopoiskId?.let {
            val kp_pojo = giveKp(it.toInt()).await()
            val rating = giveRating(it.toInt()).await()
            // result object for add to database
            val serial = Serial.build(rating, kp_pojo, cdn)
            val genres = Genres.build(kp_pojo, cdn)
            val countries = Countries.build(kp_pojo, cdn)
            //add to database
            modelDatabase.addSerialSync(serial)
            modelDatabase.addGenresSync(genres, CONTENT_TYPE)
            modelDatabase.addCountriesSync(countries, CONTENT_TYPE)
            log("add to db ${serial.ru_title} [${rating.kp}]")
        }
    }

    fun log(s: String){
        Log.i("self-test-update",s)
    }

    fun giveKp(kp_id: Int): Deferred<POJOKinopoisk?> = GlobalScope.async(Dispatchers.IO) {
        InstanceApi.kinopoisk.filmsInfo(kp_id).execute().body()
    }

    fun giveRating(kp_id: Int): Deferred<RatingPOJO> = GlobalScope.async(Dispatchers.IO){
        RatingApi.getRating(kp_id)
    }
}*/
