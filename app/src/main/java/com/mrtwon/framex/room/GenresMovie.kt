package com.mrtwon.framex.room

import androidx.room.Entity
import com.mrtwon.framex.Retrofit.Kinopoisk.POJOKinopoisk
import com.mrtwon.framex.Retrofit.VideoCdn.Movies.DataItem

@Entity
open class GenresMovie: Genres() {
    override fun toString(): String {
        return "[${kp_id}]    ${genres}"
    }

    companion object{

        fun buildOther(content: POJOKinopoisk?, kp_id: Int?, imdb_id: String?): List<GenresMovie>{
            val result = arrayListOf<GenresMovie>()

            // if genres is empty
            if(content?.data?.genres != null && content.data.genres.isEmpty()){
                result.add(GenresMovie().apply {
                    this.kp_id = kp_id
                    this.imdb_id = imdb_id
                })
                return result
            }

            for(element in content?.data?.genres!!){
                result.add(GenresMovie().apply {
                    this.kp_id = kp_id
                    this.imdb_id = imdb_id
                    genres = element?.genre
                })
            }
            return result
        }

        fun build(content: POJOKinopoisk?, cdn: DataItem): List<GenresMovie>{
            val result = arrayListOf<GenresMovie>()

            // if genres is empty
            if(content?.data?.genres != null && content.data.genres.isEmpty()){
                result.add(GenresMovie().apply {
                    this.kp_id = cdn.kinopoiskId?.toInt()
                    this.imdb_id = cdn.imdbId
                })
                return result
            }

            for(element in content?.data?.genres!!){
                result.add(GenresMovie().apply {
                    this.kp_id = cdn.kinopoiskId?.toInt()
                    this.imdb_id = cdn.imdbId
                    genres = element?.genre
                })
            }
            return result
        }

    }
}