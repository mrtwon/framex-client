package com.mrtwon.framex.Retrofit.KinopoiskRating

import android.util.Log
import com.mrtwon.framex.Retrofit.InstanceApi
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

class RatingApi {
    companion object {
        private val client = InstanceApi.okHttp
        private fun response(kp_id: Int): String? {
            val request = Request.Builder()
                .url("https://rating.kinopoisk.ru/$kp_id.xml")
                .build()
            return client.newCall(request).execute().body?.string()
        }

        private fun parsingXML(response: String): RatingPOJO {
            val result = RatingPOJO()
            val xpp = XmlPullParserFactory.newInstance().apply {
                isNamespaceAware = true
            }.newPullParser()
            xpp.setInput(StringReader(response))
            var evenType = xpp.eventType
            var tmp = "default"
            while (evenType != XmlPullParser.END_DOCUMENT) {
                if (evenType == XmlPullParser.START_TAG) {
                    tmp = xpp.name
                }
                if (evenType == XmlPullParser.TEXT) {
                    when (tmp) {
                        "kp_rating" -> {result.kp = xpp.text.toDouble()} //TODO если 0 то тоже будет null.
                        "imdb_rating" -> {result.imdb = xpp.text.toDouble()}
                    }
                }
                evenType = xpp.next()
            }
            return result
        }

        fun getRating(kp_id: Int): RatingPOJO {
            val response = response(kp_id) ?: return RatingPOJO()
            return parsingXML(response)
        }

        fun log(msg: String) {
            Log.i("self-service",msg)
        }
    }
}