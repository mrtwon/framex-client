package com.mrtwon.framex.Model

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.startandroid.MyApplication
import com.example.testbook.Retrofit.Kinopoisk.KinopoiskApi
import com.github.mrtwon.library.XmlParse
import com.mrtwon.framex.room.Database
import com.mrtwon.framex.Content.CollectionContentEnum
import com.mrtwon.framex.Content.ContentTypeEnum
import com.mrtwon.framex.Content.GenresEnum
import com.mrtwon.framex.Retrofit.Kinopoisk.Data
import com.mrtwon.framex.Retrofit.Kinopoisk.POJOKinopoisk
import com.mrtwon.framex.Retrofit.KinopoiskRating.RatingApi
import com.mrtwon.framex.Retrofit.KinopoiskRating.RatingPOJO
import com.mrtwon.framex.Retrofit.VideoCdn.VideoCdnApi
import com.mrtwon.framex.room.*
import kotlinx.coroutines.*
import java.util.*

/*
* Model Class
* Working with network and database
 */
class Model(val db: Database, private val kinopoiskApi: KinopoiskApi,
            private val videoCdnApi: VideoCdnApi, private val ratingApi: XmlParse) {
    /*
    Model Api
     */

    fun searchSerial(query: String, callback: (List<Serial>) -> Unit, noConnect: (Boolean) -> Unit){
        GlobalScope.launch(CoroutineExceptionHandler { context, exception ->
            noConnect(true)
        }) {
            val responseListSerials =
                videoCdnApi.searchSerial(query).execute().body()?.data
            val result = arrayListOf<Serial>()
            if (responseListSerials != null) {
                for (element in responseListSerials) {
                    if (element?.id != null && element.contentType != null) {
                        if (!isExistingSync(element.id, element.contentType)) {
                            continue
                        }
                    } else { continue }
                    element.kinopoiskId?.let {
                        val kp_pojo = kinopoiskApi.filmsInfo(it.toInt()).execute().body()
                        val rating = RatingApi.getRating(it.toInt())
                        result.add(
                            Serial.build(
                                rating, kp_pojo, element
                            )
                        )
                    }
                }
                callback(result)
            }
        }
    }
    fun searchMovie(query: String,callback: (List<Movie>) -> Unit, noConnect: (Boolean) -> Unit) {
        GlobalScope.launch(CoroutineExceptionHandler { context, exception ->
            noConnect(true)
        }) {
            val responseListSerials = videoCdnApi.searchMovie(query).execute().body()?.data
            val result = arrayListOf<Movie>()
            if (responseListSerials != null) {
                for (element in responseListSerials) {
                    if (element?.id != null && element.contentType != null) {
                        if (!isExistingSync(element.id, element.contentType)) {
                            continue
                        }
                    } else {
                        continue
                    }
                    element.kinopoiskId?.let {
                        val kp_pojo = kinopoiskApi.filmsInfo(it.toInt()).execute().body()
                        val rating = RatingApi.getRating(it.toInt())
                        result.add(
                            Movie.build(
                                rating, kp_pojo, element
                            )
                        )
                    }
                }
                callback(result)
            }
        }
    }

    fun checkedBlockSync(id: Int, contentType: String): Boolean{
        when(contentType){
            "tv_series" -> {
                return videoCdnApi.serialById(id).execute().body()?.data?.isEmpty() ?: true
            }
            "movie" -> {
                return videoCdnApi.movieById(id).execute().body()?.data?.isEmpty() ?: true
            }
        }
        return false
    }


    fun giveKpSync(kp_id: Int): POJOKinopoisk? = kinopoiskApi.filmsInfo(kp_id).execute().body()
    fun giveRatingSync(kp_id: Int): RatingPOJO = ratingApi.request(kp_id.toString(), RatingPOJO::class.java)



    /*
    Model Database
     */

    @DelicateCoroutinesApi
    fun getTopByGenresEnum(genres: GenresEnum, content: ContentTypeEnum, callback: (List<Content>) -> Unit){
        GlobalScope.launch {
            when(content){
                ContentTypeEnum.SERIAL ->{
                    val resultDB = db.dao().getTopSerial(genres.toString())
                    callback(resultDB)
                }
                ContentTypeEnum.MOVIE -> {
                    val resultDB = db.dao().getTopMovie(genres.toString())
                    callback(resultDB)
                }
            }
        }
    }
    @DelicateCoroutinesApi
    fun getTopByCollectionEnum(collectionEnum: CollectionContentEnum, content: ContentTypeEnum, callback: (List<Content>) -> Unit){
        GlobalScope.launch {
            val date = Calendar.getInstance().get(Calendar.YEAR)
            when(content){
                ContentTypeEnum.SERIAL -> {
                    val result = db.dao().getTopSerialByCurrentYear(date)
                    callback(result)
                }
                ContentTypeEnum.MOVIE -> {
                    val result = db.dao().getTopMovieByCurrentYear(date)
                    callback(result)
                }
            }
        }
    }
    @DelicateCoroutinesApi
    fun getAboutSerial(id: Int, callback: (SerialWithGenres) -> Unit){
        GlobalScope.launch {
            val result = db.dao().getAboutSerial(id)
            Log.i("self-model","description length = ${result.description?.length}")
            callback(result)
        }
    }

    @DelicateCoroutinesApi
    fun getAboutMovie(id: Int, callback: (MovieWithGenres) -> Unit){
        GlobalScope.launch {
            val result = db.dao().getAboutMovie(id)
            Log.i("self-model","description length = ${result.description?.length}")
            callback(result)
        }
    }

    fun getFavorite(callback: (List<Content>) -> Unit) {
        GlobalScope.launch {
            val listFavorite = db.dao().getFavorite()
            val result = arrayListOf<Content>()
            Log.i("self-model", "listFavorite = ${listFavorite.size}")
            for (favorite in listFavorite) {
                favorite.idRef?.let {
                    when (favorite.contentType) {
                        "movie" -> {
                            result.add(db.dao().getMovie(it) as Content)
                        }
                        "tv_series" -> {
                            Log.i("self-model","seria id = $it")
                            result.add(db.dao().getSerial(it) as Content)
                        }
                        else -> {
                        }
                    }
                }
            }
            callback(result)
        }
    }
    fun addFavorite(id: Int, contentType: String){
        GlobalScope.launch {
            val isExistElement = db.dao().getFavoriteElement(id, contentType) == null
            if (isExistElement) {
                val favoriteResult = Favorite().apply {
                    this.idRef = id
                    this.contentType = contentType
                }
                db.dao().addFavorite(favoriteResult)
            }
        }
    }
    fun removeFavorite(id: Int, contentType: String) {
        GlobalScope.launch {
            db.dao().deleteFavoriteElement(id, contentType)
        }
    }
    fun isFavorite(id: Int, contentType: String, callback: (Boolean) -> Unit){
        GlobalScope.launch {
            val result = db.dao().getFavoriteElement(id, contentType) != null
            callback(result)
        }
    }
    suspend fun testLiveData(): LiveData<List<Favorite>>{
        return GlobalScope.async{
            db.dao().getFavoriteLiveData()
        }.await()
    }
    fun getVideoLink(id: Int, contentType: String, callback: (Content) -> Unit){
        GlobalScope.launch {
            when (contentType) {
                "tv_series" -> {
                    val result = db.dao().getSerialVideoLink(id)
                    callback(result)
                }
                "movie" -> {
                    val result = db.dao().getMovieVideoLink(id)
                    callback(result)
                }
            }
        }
    }
    fun addRecent(id: Int, contentType: String) {
        GlobalScope.launch {
            val recent: Recent? = db.dao().getRecentElement(id, contentType)
            val time = getSecondTime()
            if (recent == null) {
                val resultRecent = Recent().apply {
                    this.idRef = id
                    this.contentType = contentType
                    this.time = time
                }
                db.dao().insertRecent(resultRecent)
            }else {
                recent.apply {
                    this.time = time
                }
                db.dao().updateRecent(recent)
            }
        }
    }
    fun getRecentList(callback: (List<Recent>) -> Unit){
        GlobalScope.launch {
            val recentList = db.dao().getRecentList()
            callback(recentList)
        }
    }
    fun getRecentContent(callback: (List<Content>) -> Unit) {
        GlobalScope.launch {
            val recentList = db.dao().getRecentList()
            val resultContent = arrayListOf<Content>()
            for (recent in recentList) {
                recent.idRef?.let {
                    val content: Content? = when (recent.contentType) {
                        "tv_series" -> {
                            db.dao().getSerial(it)
                        }
                        "movie" -> {
                            db.dao().getMovie(it)
                        }
                        else -> {
                            null
                        }
                    }

                    if (content != null) {
                        resultContent.add(content)
                    }
                }
            }
            callback(resultContent)
        }
    }
    fun getSizeDatabase(callback: (DatabaseSize) -> Unit){
        GlobalScope.launch {
            val serial = db.dao().getCountSerial().count
            val movie = db.dao().getCountMovie().count
            val result = DatabaseSize(serial, movie)
            callback(result)
        }
    }

    fun getSearchResult(requestString: String, callback: (List<Content>) -> Unit){
        GlobalScope.launch {
            val result = arrayListOf<Content>()
            val stringSearch = "%$requestString%"
            val searchMovie = db.dao().searchTitleMovie(stringSearch)
            val searchSerial = db.dao().searchTitleSerial(stringSearch)
            Log.i("self-search","[$stringSearch] size m = ${searchMovie.size} and s = ${searchSerial.size}")
            result.addAll(searchMovie)
            result.addAll(searchSerial)
            callback(result)
        }
    }
    fun searchDescription(stringSearch: String, callback: (List<Content>) -> Unit){
        GlobalScope.launch {
            val result = arrayListOf<Content>()
            val searchSerial = db.dao().searchDescriptionSerial("%$stringSearch%")
            val searchMovie = db.dao().searchDescriptionMovie("%$stringSearch%")

            result.addAll(searchMovie)
            result.addAll(searchSerial)

            for(elem in result){
                elem.description_lower?.let {
                    elem.description_lower = htmlPrompt(stringSearch, it)
                }
            }

            callback(result)
        }
    }
    fun <T : Content> addListContent(list: List<T>, contentType: String){
        when(contentType){
            "movie" -> {
                db.dao().insertListMovie(list as List<Movie>)
            }
            "tv_series" -> {
                db.dao().insertListSerial(list as List<Serial>)
            }
        }
    }
    fun isExistingSync(id: Int, contentType: String): Boolean{
        return when(contentType){
            "movie" -> {
                return db.dao().isExistingMovie(id) == null
            }
            "tv_series" -> {
                return db.dao().isExistingSerial(id) == null
            }
            else -> false
        }
    }

    fun <T: Genres> addGenresSync(genres: List<T>, contentType: String){
        when(contentType){
            "movie" -> {
                db.dao().addGenresMovie(genres as List<GenresMovie>)
            }
            "tv_series" -> {
                db.dao().addGenresSerial(genres as List<Genres>)
            }
        }
    }
    fun <T : Countries> addCountriesSync(countries: List<T>, contentType: String){
        when(contentType){
            "movie" -> {
                db.dao().addCountriesMovie(countries as List<CountriesMovie>)
            }
            "tv_series" -> {
                db.dao().addCountriesSerial(countries as List<Countries>)
            }
        }
    }
    fun addSerialSync(content: Serial){
        db.dao().addSerial(content)
    }
    fun addMovieSync(content: Movie){
        db.dao().addMovie(content)
    }
    fun <T : Content> createdNewContent(content: T, contentType: String){
        when(contentType){
            "tv_series" ->{
                val pojo_kp = giveKpSync(content.kp_id!!)
                val genres = Genres.buildOther(pojo_kp, content.kp_id!!, content.imdb_id)
                val countries = Countries.buildOther(pojo_kp, content.kp_id, content.imdb_id)
                addCountriesSync(countries, contentType)
                addGenresSync(genres, contentType)
            }
            "movie" -> {
                val pojo_kp = giveKpSync(content.kp_id!!)
                val genres = GenresMovie.buildOther(pojo_kp, content.kp_id!!, content.imdb_id)
                val countries = CountriesMovie.buildOther(pojo_kp, content.kp_id, content.imdb_id)
                addCountriesSync(countries, contentType)
                addGenresSync(genres, contentType)
            }
        }
    }

    fun <T : Content> createdNewContents(content: List<T>, contentType: String){
        for (one_content in content) {
            createdNewContent(one_content, contentType)
            addListContent(content, contentType)
        }
    }
    fun <T : Content>createNewContentFromMix(serial: List<T>?, movie: List<T>?, resultBoolean: (Boolean) -> Unit){
        GlobalScope.launch(CoroutineExceptionHandler { context, exception ->
            resultBoolean(false)
        }){
            serial?.let {
                createdNewContents(serial, "tv_series")
            }
            movie?.let {
                createdNewContents(movie, "movie")
            }
            resultBoolean(true)
        }
    }

    fun getNotificationList(callback: (List<Notification>) -> Unit){
        GlobalScope.launch(Dispatchers.IO) {
            val result = db.dao().getNotification()
            callback(result)
        }
    }
    fun getNotificationListLiveData(): LiveData<List<Notification>> = db.dao().getNotificationLiveData()
    fun getSubscriptionListLiveData(): LiveData<List<Subscription>> = db.dao().getSubscriptionsLiveData()

    fun getSubscriptionList(callback: (List<Serial>) -> Unit){
        GlobalScope.launch(Dispatchers.IO) {
            val result = arrayListOf<Serial>()
            val subscriptions = db.dao().getSubscriptions()
            for(subscription in subscriptions){
                val serial = db.dao().getSerial(subscription.content_id)
                result.add(serial)
            }
            callback(result)
        }
    }
    fun removeNotification(notification: Notification){
        GlobalScope.launch {
            val result = db.dao().deleteNotification(notification)
        }
    }
    fun removeSubscription(id: Int){
        GlobalScope.launch {
            val result = db.dao().deleteSubscription(id)
        }
    }
    fun subscriptionIf(id: Int){
        GlobalScope.launch {
            val isExisting = db.dao().getSubscriptionById(id)
            if(isExisting == null){
                val serial = db.dao().getSerial(id)
                val count = allCountEpisodeSerialSync(id)
                if(count != null){
                    db.dao().addSubscription(Subscription().apply {
                        this.count = count
                        this.content_id = serial.id
                    })
                }
            }else{
                db.dao().deleteSubscription(id)
            }
        }
    }
    fun getSubscriptionByIdLiveData(id: Int): LiveData<Subscription> = db.dao().getSubscriptionByIdLiveData(id)
    fun allCountEpisodeSerialSync(id: Int): Int?{
        val response = videoCdnApi.serialById(id).execute().body()
        return response?.data?.get(0)?.episodeCount
    }


    /*
    Helper Functions
     */
    fun htmlPrompt(findString: String, data: String): String {
        return data.replace(findString, "<b>$findString</b>")
    }

    private fun getSecondTime(): Int{
        return (Date().time/1000).toInt()
    }

}