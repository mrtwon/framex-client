package com.mrtwon.framex.Retrofit.Kinopoisk

import com.google.gson.annotations.SerializedName
data class POJOKinopoisk(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("externalId")
	val externalId: ExternalId? = null
)

data class CountriesItem(
	@field:SerializedName("country")
    val country: String? = null
)

data class SeasonsItem(

	@field:SerializedName("number")
	val number: Int? = null,

	@field:SerializedName("episodes")
	val episodes: List<EpisodesItem?>? = null
)

data class ExternalId(

	@field:SerializedName("imdbId")
	val imdbId: String? = null
)

data class GenresItem(

	@field:SerializedName("genre")
	val genre: String? = null
)

data class EpisodesItem(

	@field:SerializedName("nameRu")
	val nameRu: String? = null,

	@field:SerializedName("releaseDate")
	val releaseDate: String? = null,

	@field:SerializedName("seasonNumber")
	val seasonNumber: Int? = null,

	@field:SerializedName("nameEn")
	val nameEn: String? = null,

	@field:SerializedName("synopsis")
	var synopsis: String? = null,

	@field:SerializedName("episodeNumber")
	var episodeNumber: Int? = null
)

data class Data(

	@field:SerializedName("premiereDigital")
	val premiereDigital: Any? = null,

	@field:SerializedName("premiereBluRay")
	val premiereBluRay: Any? = null,

	@field:SerializedName("year")
	val year: String? = null,

	@field:SerializedName("premiereDvd")
	val premiereDvd: String? = null,

	@field:SerializedName("filmLength")
	val filmLength: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("facts")
	val facts: List<String?>? = null,

	@field:SerializedName("nameRu")
	val nameRu: String? = null,

	@field:SerializedName("posterUrl")
	val posterUrl: String? = null,

	@field:SerializedName("genres")
	val genres: List<GenresItem?>? = null,

	@field:SerializedName("ratingMpaa")
	val ratingMpaa: Any? = null,

	@field:SerializedName("ratingAgeLimits")
	val ratingAgeLimits: Int? = null,

	@field:SerializedName("seasons")
	val seasons: List<SeasonsItem?>? = null,

	@field:SerializedName("distributors")
	val distributors: Any? = null,

	@field:SerializedName("nameEn")
	val nameEn: String? = null,

	@field:SerializedName("countries")
	val countries: List<CountriesItem?>? = null,

	@field:SerializedName("premiereWorld")
	val premiereWorld: String? = null,

	@field:SerializedName("webUrl")
	val webUrl: String? = null,

	@field:SerializedName("premiereRu")
	val premiereRu: String? = null,

	@field:SerializedName("distributorRelease")
	val distributorRelease: String? = null,

	@field:SerializedName("filmId")
	val filmId: Int? = null,

	@field:SerializedName("premiereWorldCountry")
	val premiereWorldCountry: String? = null,

	@field:SerializedName("posterUrlPreview")
	val posterUrlPreview: String? = null,

	@field:SerializedName("slogan")
	val slogan: String? = null
)
