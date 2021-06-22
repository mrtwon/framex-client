package com.mrtwon.framex.room

import androidx.room.Entity
import com.mrtwon.framex.Retrofit.Kinopoisk.POJOKinopoisk
import com.mrtwon.framex.Retrofit.VideoCdn.Movies.DataItem

@Entity
class CountriesMovie: Countries() {
    companion object{
        fun buildOther(content: POJOKinopoisk?, kp_id: Int?, imdb_id: String?): List<CountriesMovie>{
            val result = arrayListOf<CountriesMovie>()

            //if country is empty
            if(content?.data?.countries != null && content.data.countries.isEmpty()){
                result.add(CountriesMovie().apply {
                    this.kp_id = kp_id
                    this.imdb_id = imdb_id
                })
                return result
            }

            for(element in content!!.data!!.countries!!){
                result.add(CountriesMovie().apply {
                    this.kp_id = kp_id
                    this.imdb_id = imdb_id
                    country = element?.country
                })
            }
            return result
        }
        fun build(content: POJOKinopoisk?, cdn: DataItem): List<CountriesMovie>{
            val result = arrayListOf<CountriesMovie>()

            //if country is empty
            if(content?.data?.countries == null){
                result.add(CountriesMovie().apply {
                    kp_id = cdn.kinopoiskId!!.toInt()
                    imdb_id = cdn.imdbId
                })
                return result
            }

            for(element in content.data.countries){
                result.add(CountriesMovie().apply {
                    kp_id = cdn.kinopoiskId!!.toInt()
                    imdb_id = cdn.imdbId
                    country = element?.country
                })
            }
            return result
        }
    }
}