package com.mrtwon.framex.room
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mrtwon.framex.Retrofit.Kinopoisk.POJOKinopoisk
import com.mrtwon.framex.Retrofit.VideoCdn.TvSeries.DataItem

@Entity
open class Genres: OnlyGenres() {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var kp_id: Int? = null
    var imdb_id: String? = null

    companion object {
        fun buildOther(content: POJOKinopoisk?, kp_id: Int?, imdb_id: String?): List<Genres> {
            val result = arrayListOf<Genres>()

            // if genres is empty
            if (content?.data?.genres != null && content.data.genres.isEmpty()) {
                result.add(Genres().apply {
                    this.kp_id = kp_id
                    this.imdb_id = imdb_id
                })
                return result
            }

            for (element in content!!.data!!.genres!!) {
                result.add(Genres().apply {
                    this.kp_id = kp_id
                    this.imdb_id = imdb_id
                    genres = element?.genre
                })
            }
            return result
        }

        fun build(content: POJOKinopoisk?, cdn: DataItem): List<Genres> {
            val result = arrayListOf<Genres>()

            // if genres is empty
            if (content?.data?.genres != null && content.data.genres.isEmpty()) {
                result.add(Genres().apply {
                    this.kp_id = cdn.kinopoiskId?.toInt()
                    this.imdb_id = cdn.imdbId
                })
                return result
            }

            for (element in content!!.data!!.genres!!) {
                result.add(Genres().apply {
                    this.kp_id = cdn.kinopoiskId?.toInt()
                    this.imdb_id = cdn.imdbId
                    genres = element?.genre
                })
            }
            return result

        }
    }
}




