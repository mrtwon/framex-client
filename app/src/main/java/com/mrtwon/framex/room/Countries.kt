package com.mrtwon.framex.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mrtwon.framex.Retrofit.Kinopoisk.POJOKinopoisk
import com.mrtwon.framex.Retrofit.VideoCdn.TvSeries.DataItem

@Entity
open class Countries {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var kp_id: Int? = null
    var imdb_id: String? = null
    var country: String? = null

    companion object {
        fun buildOther(content: POJOKinopoisk?, kp_id: Int?, imdb_id: String?): List<Countries> {
            val result = arrayListOf<Countries>()

            //if country is empty
            if (content?.data?.countries != null && content.data.countries.isEmpty()) {
                result.add(Countries().apply {
                    this.kp_id = kp_id
                    this.imdb_id = imdb_id
                })
                return result
            }

            for (element in content!!.data!!.countries!!) {
                result.add(Countries().apply {
                    this.kp_id = kp_id
                    this.imdb_id = imdb_id
                    country = element?.country
                })
            }
            return result
        }

        fun build(content: POJOKinopoisk?, cdn: DataItem): List<Countries> {
            val result = arrayListOf<Countries>()

            //if country is empty
            if (content?.data?.countries != null && content.data.countries.isEmpty()) {
                result.add(Countries().apply {
                    this.kp_id = cdn.kinopoiskId?.toInt()
                    this.imdb_id = cdn.imdbId
                })
                return result
            }

            for (element in content!!.data!!.countries!!) {
                result.add(Countries().apply {
                    this.kp_id = cdn.kinopoiskId?.toInt()
                    this.imdb_id = cdn.imdbId
                    country = element?.country
                })
            }
            return result
        }
    }
}