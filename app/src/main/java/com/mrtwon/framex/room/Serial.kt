package com.mrtwon.framex.room
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.mrtwon.framex.Retrofit.Kinopoisk.POJOKinopoisk
import com.mrtwon.framex.Retrofit.KinopoiskRating.RatingPOJO
import com.mrtwon.framex.Retrofit.VideoCdn.TvSeries.DataItem

@Entity(indices =
// array index for Serial
    [Index(value = arrayOf("kp_id"), name = "idx_serial_kp_id", unique = false)]
)
open class Serial: Content(){
    var seasonCount: Int? = null
    var filmLength: Int? = null
    var ratingMpaa: String? = null
    var kp_site: String? = null

    companion object {
        fun build(rating: RatingPOJO?, kp: POJOKinopoisk?, cdn: DataItem): Serial =
            Serial().apply {
                //object
                val kpData = kp?.data ?: POJOKinopoisk().data
                val ratingData = rating ?: RatingPOJO()
                //rating
                kp_rating = ratingData.kp
                imdb_rating = ratingData.imdb
                //cdn
                id = cdn.id!!
                kp_id = cdn.kinopoiskId?.toInt()
                imdb_id = cdn.imdbId
                ru_title = cdn.ruTitle
                ru_title_lower = ru_title?.toLowerCase()
                orig_title = cdn.origTitle
                contentType = cdn.contentType
                iframe_src = cdn.iframeSrc
                seasonCount = cdn.seasonCount
                //kp
                year = getDate(kpData?.year)
                filmLength = convertTime(kpData?.filmLength)
                description = kpData?.description
                description_lower = description?.toLowerCase()
                ratingMpaa = kpData?.ratingMpaa?.toString()
                poster = kpData?.posterUrl
                kp_site = kpData?.webUrl
                return this
            }
        private fun convertTime(time: String?): Int?{
            if(time == null)
                return null
            var summaryMin = 0
            val splitTime = time.split(":")
            summaryMin += splitTime[0].toInt() * 60
            summaryMin += splitTime[1].toInt()
            return summaryMin
        }
    }
    private fun getDate(s: String?): Int?{
        if(s == null || s.isEmpty()){
            return null
        }
        return s.split("-")[0].toInt()
    }

    override fun toString(): String {
        return "id: $id  |  kp_id $kp_id  imdb_id  $imdb_id  | Title $ru_title"
    }
}