package com.mrtwon.framex.Retrofit.VideoCdn
import com.mrtwon.framex.Retrofit.VideoCdn.Movies.POJOVideoCdnMovie
import com.mrtwon.framex.Retrofit.VideoCdn.TvSeries.POJOVideoCdnTS
import retrofit2.Call
import retrofit2.http.*

interface VideoCdnApi {
    @Headers("Content-Type: application/json")
    @GET("movies?api_token=gizTFEdcm6OFdqeF74t9Owbtf98DK1KT&ordering=created&direction=desc&limit=100")
    fun nextPageMovie(@Query("page") page: Int): Call<POJOVideoCdnMovie>

    @Headers("Content-Type: application/json")
    @GET("tv-series?api_token=gizTFEdcm6OFdqeF74t9Owbtf98DK1KT&ordering=created&direction=desc&limit=100")
    fun nextPageSerial(@Query("page") page: Int): Call<POJOVideoCdnTS>

    @Headers("Content-Type: application/json")
    @GET("movies?api_token=gizTFEdcm6OFdqeF74t9Owbtf98DK1KT")
    fun getByImdb(@Query("imdb_id") imdb_id: String): Call<POJOVideoCdnMovie>

    @Headers("Content-Type: application/json")
    @GET("movies?api_token=gizTFEdcm6OFdqeF74t9Owbtf98DK1KT")
    fun getByKp(@Query("kinopoisk_id") kp_id: Int): Call<POJOVideoCdnMovie>

    @Headers("Content-Type: application/json")
    @GET("tv-series?api_token=gizTFEdcm6OFdqeF74t9Owbtf98DK1KT")
    fun getByImdbSerial(@Query("imdb_id") imdb_id: String): Call<POJOVideoCdnMovie>

    @Headers("Content-Type: application/json")
    @GET("tv-series?api_token=gizTFEdcm6OFdqeF74t9Owbtf98DK1KT")
    fun getByKpSerial(@Query("kinopoisk_id") kp_id: Int): Call<POJOVideoCdnMovie>

    @Headers("Content-Type: application/json")
    @GET("tv-series?api_token=gizTFEdcm6OFdqeF74t9Owbtf98DK1KT")
    fun searchSerial(@Query("query") query: String): Call<POJOVideoCdnTS>

    @Headers("Content-Type: application/json")
    @GET("movies?api_token=gizTFEdcm6OFdqeF74t9Owbtf98DK1KT")
    fun searchMovie(@Query("query") query: String): Call<POJOVideoCdnMovie>

    @Headers("Content-Type: application/json")
    @GET("tv-series?api_token=gizTFEdcm6OFdqeF74t9Owbtf98DK1KT")
    fun serialById(@Query("id") id: Int): Call<POJOVideoCdnTS>

    @Headers("Content-Type: application/json")
    @GET("movies?api_token=gizTFEdcm6OFdqeF74t9Owbtf98DK1KT")
    fun movieById(@Query("id") id: Int): Call<POJOVideoCdnMovie>



    /*@Headers("Content-Type: application/json")
    @GET("short?api_token=gizTFEdcm6OFdqeF74t9Owbtf98DK1KT")
    fun getSerial(@Query("kinopoisk_id") id: Int): Call<POJOVideoCdn>


    @Headers("Content-Type: application/json")
    @GET("tv-series?api_token=gizTFEdcm6OFdqeF74t9Owbtf98DK1KT")
    fun tvSeriesYear(@Query("year") id: Int,
                     @Query("page") page: Int,
                     @Query("limit") limit: Int): Call<POJOVideoCdnTS>


    @Headers("Content-Type: application/json")
    @GET("tv-series?api_token=gizTFEdcm6OFdqeF74t9Owbtf98DK1KT&ordering=start_date&direction=asc")
    fun tvSeriesOld(@Query("page") page:Int,
                    @Query("limit") limit: Int): Call<POJOVideoCdnTS>


    @Headers("Content-Type: application/json")
    @GET("movies?api_token=gizTFEdcm6OFdqeF74t9Owbtf98DK1KT&ordering=released&direction=asc")
    fun moviesOld(@Query("page") page: Int = 1, @Query("limit") limit: Int = 10): Call<POJOVideoCdnMovie>*/
}