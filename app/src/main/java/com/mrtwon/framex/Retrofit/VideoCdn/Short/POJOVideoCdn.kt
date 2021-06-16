package com.mrtwon.framex.Retrofit.VideoCdn.Short

import com.google.gson.annotations.SerializedName

data class POJOVideoCdn(

    @field:SerializedName("per_page")
	val perPage: Int? = null,

    @field:SerializedName("data")
	val data: List<DataItem?>? = null,

    @field:SerializedName("last_page")
	val lastPage: Int? = null,

    @field:SerializedName("next_page_url")
	val nextPageUrl: Any? = null,

    @field:SerializedName("prev_page_url")
	val prevPageUrl: Any? = null,

    @field:SerializedName("result")
	val result: Boolean? = null,

    @field:SerializedName("first_page_url")
	val firstPageUrl: String? = null,

    @field:SerializedName("path")
	val path: String? = null,

    @field:SerializedName("total")
	val total: Int? = null,

    @field:SerializedName("last_page_url")
	val lastPageUrl: String? = null,

    @field:SerializedName("from")
	val from: Int? = null,

    @field:SerializedName("to")
	val to: Int? = null,

    @field:SerializedName("current_page")
	val currentPage: Int? = null
)

data class DataItem(

	@field:SerializedName("add")
	val add: String? = null,

	@field:SerializedName("imdb_id")
	val imdbId: String? = null,

	@field:SerializedName("orig_title")
	val origTitle: String? = null,

	@field:SerializedName("year")
	val year: String? = null,

	@field:SerializedName("update")
	val update: String? = null,

	@field:SerializedName("iframe_src")
	val iframeSrc: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("kp_id")
	val kpId: String? = null,

	@field:SerializedName("episodes_count")
	val episodesCount: Int? = null,

	@field:SerializedName("seasons_count")
	val seasonsCount: Int? = null,

	@field:SerializedName("translations")
	val translations: List<String?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("world_art_id")
	val worldArtId: Any? = null,

	@field:SerializedName("episodes")
	val episodes: Int? = null
)
