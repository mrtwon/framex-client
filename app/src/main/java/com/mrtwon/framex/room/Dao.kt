package com.mrtwon.framex.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {
    @Query("SELECT * FROM Movie WHERE kp_id = :kp_id or imdb_id = :imdb_id")
    fun movieIsAlready(kp_id: Int, imdb_id: String): Movie?
    @Query("SELECT * FROM Movie WHERE kp_id = :kp_id")
    fun movieIsAlreadyKpId(kp_id: Int): Movie?
    @Query("SELECT * FROM Movie WHERE imdb_id = :imdb_id")
    fun movieIsAlreadyImdbId(imdb_id: String): Movie?
    @Query("SELECT * FROM Movie")
    fun getAllMovie(): List<Movie>
    @Query("SELECT * FROM Serial")
    fun getAllSerial(): List<Serial>
    @Query("UPDATE Movie SET id = :newId WHERE kp_id = :kp_id")
    fun updateByKp(newId: Int, kp_id: Int)
    @Query("UPDATE Movie SET id = :newId WHERE imdb_id = :imdb_id")
    fun updateByImdb(newId: Int, imdb_id: String)

    @Query("UPDATE Serial SET id = :newId WHERE kp_id = :kp_id")
    fun updateByKpSerial(newId: Int, kp_id: Int)
    @Query("UPDATE Serial SET id = :newId WHERE imdb_id = :imdb_id")
    fun updateByImdbSerial(newId: Int, imdb_id: String)

    @Query("UPDATE Serial SET id = :newId WHERE id = :oldId")
    fun updateByUidSerial(newId: Int, oldId: Int)


    @Query("UPDATE Movie SET id = :newId WHERE imdb_id = :imdb_id and kp_id = :kp_id")
    fun updateByFullId(newId: Int, imdb_id: String, kp_id: Int)
    @Query("SELECT * FROM Movie WHERE id = :id")
    fun movieIsAlreadyById(id: Int): Movie?
    @Query("SELECT * FROM Serial WHERE id = :id")
    fun serialIsAlreadyById(id: Int): Movie?
    @Insert
    fun addMovie(movie: Movie)
    @Insert
    fun addSerial(serial: Serial)

    @Query("SELECT  Movie.id, Movie.kp_id, Movie.imdb_id, Movie.ru_title, Movie.orig_title, Movie.poster, Movie.kp_rating, Movie.imdb_rating  FROM Movie JOIN GenresMovie ON Movie.kp_id = GenresMovie.kp_id WHERE genres = :genres  ORDER BY kp_rating DESC LIMIT 100")
    fun getTopMovie(genres: String): List<Content>

    @Query("SELECT  Serial.id, Serial.kp_id, Serial.imdb_id, Serial.ru_title, Serial.orig_title, Serial.poster, Serial.kp_rating, Serial.imdb_rating FROM Serial JOIN Genres ON Serial.kp_id = Genres.kp_id WHERE genres = :genres ORDER BY kp_rating DESC LIMIT 100")
    fun getTopSerial(genres: String): List<Content>

    @Query("SELECT * FROM Serial WHERE year = :year ORDER BY kp_rating DESC")
    fun getTopSerialByCurrentYear(year: Int): List<Content>

    @Query("SELECT * FROM Movie WHERE year = :year ORDER BY kp_rating DESC")
    fun getTopMovieByCurrentYear(year: Int): List<Content>

    @Query("SELECT Serial.id, Serial.kp_id, Serial.imdb_id, Serial.ru_title, Serial.orig_title, Serial.poster, Serial.kp_rating, Serial.imdb_rating, Serial.description,Serial.year, GROUP_CONCAT(Genres.genres) as genres FROM Serial JOIN Genres ON Serial.kp_id = Genres.kp_id WHERE Serial.id = :id")
    fun getAboutSerial(id: Int): SerialWithGenres

    @Query("SELECT Movie.id, Movie.kp_id, Movie.imdb_id, Movie.ru_title, Movie.orig_title, Movie.poster, Movie.kp_rating, Movie.imdb_rating, Movie.description, Movie.year, GROUP_CONCAT(GenresMovie.genres) as genres FROM Movie JOIN GenresMovie ON Movie.kp_id = GenresMovie.kp_id WHERE Movie.id = :id")
    fun getAboutMovie(id: Int): MovieWithGenres

    @Query("SELECT * FROM Favorite")
    fun getFavorite(): List<Favorite>
    @Query("SELECT * FROM Favorite WHERE id_ref = :id AND contentType = :contentType")
    fun getFavoriteElement(id: Int, contentType: String): Favorite?

    @Query("SELECT * FROM Movie WHERE id = :id")
    fun getMovie(id: Int): Movie

    @Insert
    fun addFavorite(favorite: Favorite)

    @Query("DELETE FROM Favorite WHERE id_ref = :id AND contentType = :contentType")
    fun deleteFavoriteElement(id: Int, contentType: String)

    @Query("SELECT * FROM Serial WHERE id = :id")
    fun getSerial(id: Int): Serial

    @Query("SELECT * FROM Favorite")
    fun getFavoriteLiveData(): LiveData<List<Favorite>>

    @Query("SELECT id, iframe_src FROM Movie WHERE id = :id")
    fun getMovieVideoLink(id: Int): Content

    @Query("SELECT id, iframe_src FROM Serial WHERE id = :id")
    fun getSerialVideoLink(id: Int): Content

    @Insert
    fun insertRecent(recent: Recent)

    @Update
    fun updateRecent(recent: Recent)

    @Query("SELECT * FROM Recent WHERE id_ref = :idRef and contentType = :contentType")
    fun getRecentElement(idRef: Int, contentType: String): Recent?

    @Query("SELECT * FROM Recent ORDER BY time DESC")
    fun getRecentList(): List<Recent>

    @Query("SELECT count(*) as count FROM Movie")
    fun getCountMovie(): CountContent

    @Query("SELECT count(*) as count FROM Serial")
    fun getCountSerial(): CountContent

    @Update
    fun updateSerial(serial: Serial)

    @Update
    fun updateMovie(movie: Movie)

    @Query("SELECT * FROM Movie WHERE ru_title_lower LIKE :stringSearch LIMIT 20")
    fun searchTitleMovie(stringSearch: String): List<Content>

    @Query("SELECT * FROM Serial WHERE ru_title_lower LIKE :stringSearch LIMIT 20")
    fun searchTitleSerial(stringSearch: String): List<Content>

    @Query("SELECT * FROM Serial WHERE description_lower LIKE :stringSearch LIMIT 20")
    fun searchDescriptionSerial(stringSearch: String): List<Content>

    @Query("SELECT * FROM Movie WHERE description_lower LIKE :stringSearch LIMIT 20")
    fun searchDescriptionMovie(stringSearch: String): List<Content>

    @Insert
    fun insertListSerial(list: List<Serial>)
    @Insert
    fun insertListMovie(list: List<Movie>)

    @Query("SELECT * FROM Movie WHERE id = :id")
    fun isExistingMovie(id: Int): Movie?

    @Query("SELECT * FROM Serial WHERE id = :id")
    fun isExistingSerial(id: Int): Serial?
    @Insert
    fun addGenresMovie(genres: List<GenresMovie>)
    @Insert
    fun addGenresSerial(genres: List<Genres>)

    @Insert
    fun addCountriesMovie(countries: List<CountriesMovie>)
    @Insert
    fun addCountriesSerial(countries: List<Countries>)

    @Query("SELECT id, kp_id FROM (SELECT *, count(kp_id) as count_id FROM Movie GROUP BY kp_id ) WHERE count_id > 1")
    fun stepOneMovie(): List<Content>

    @Query("SELECT id, kp_id FROM (SELECT *, count(kp_id) as count_id FROM Serial GROUP BY kp_id ) WHERE count_id > 1")
    fun stepOneSerial(): List<Content>

    @Query("SELECT * FROM Serial WHERE kp_id = :kp_id")
    fun stepOneGetSerial(kp_id: Int): List<Serial>

    @Query("SELECT * FROM Movie WHERE kp_id = :kp_id")
    fun stepOneGetMovie(kp_id: Int): List<Movie>

    @Query("DELETE FROM Serial WHERE id = :id")
    fun stepOneRemoveByIdSerial(id: Int)

    @Query("DELETE FROM Movie WHERE id = :id")
    fun stepOneRemoveByIdMovie(id: Int)

    @Query("UPDATE Serial SET id = :newId WHERE id = :oldId")
    fun stepOneUpdateByIdSerial(oldId: Int, newId: Int)

    @Query("UPDATE Movie SET id = :newId WHERE id = :oldId")
    fun stepOneUpdateByIdMovie(oldId: Int, newId: Int)

    //step two

    @Query("SELECT id, iframe_src FROM Movie")
    fun stepTwoGetAllMovie(): List<Content>

    @Query("SELECT id, iframe_src FROM Serial")
    fun stepTwoGetAllSerial(): List<Content>

    @Query("SELECT id, kp_id, imdb_id FROM Serial WHERE kp_id NOT IN (SELECT kp_id FROM Genres)")
    fun stepThreeGenresForUpdatingSerial(): List<Content>

    @Query("SELECT id, kp_id, imdb_id FROM Movie WHERE kp_id NOT IN (SELECT kp_id FROM GenresMovie)")
    fun stepThreeGenresForUpdatingMovie(): List<Content>

    @Query("SELECT id, kp_id, imdb_id FROM Serial WHERE kp_id NOT IN (SELECT kp_id FROM Countries)")
    fun stepThreeCountriesForUpdateSerial(): List<Content>

    @Query("SELECT id, kp_id, imdb_id FROM Movie WHERE kp_id NOT IN (SELECT kp_id FROM CountriesMovie)")
    fun stepThreeCountriesForUpdateMovie(): List<Content>

    @Insert
    fun insertGenresSerial(genres: Genres)
    @Insert
    fun insertCountriesSerial(countries: Countries)

    @Insert
    fun insertGenresMovie(genres: GenresMovie)
    @Insert
    fun insertCountriesMovie(countries: CountriesMovie)

    // function for subscription table
    @Query("SELECT * FROM Subscription")
    fun getSubscriptions(): List<Subscription>
    @Query("SELECT * FROM Subscription WHERE content_id = :id")
    fun getSubscriptionById(id: Int): Subscription?
    @Query("SELECT * FROM Subscription WHERE content_id = :id")
    fun getSubscriptionByIdLiveData(id: Int): LiveData<Subscription>
    @Query("SELECT * FROM Subscription")
    fun getSubscriptionsLiveData(): LiveData<List<Subscription>>

    @Insert
    fun addSubscription(subscription: Subscription)

    @Update
    fun updateSubscription(subscription: Subscription)

    @Query("DELETE FROM subscription WHERE content_id = :id")
    fun deleteSubscription(id: Int)

    // function for notification table
    @Query("SELECT * FROM Notification")
    fun getNotification(): List<Notification>

    @Query("SELECT * FROM Notification")
    fun getNotificationLiveData(): LiveData<List<Notification>>

    @Insert
    fun insertNotifications(notification: List<Notification>)

    @Insert
    fun insertNotification(notification: Notification)

    @Delete
    fun deleteNotification(notification: Notification)
}