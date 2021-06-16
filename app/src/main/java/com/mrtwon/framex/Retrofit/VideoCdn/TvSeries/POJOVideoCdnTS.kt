package com.mrtwon.framex.Retrofit.VideoCdn.TvSeries

import com.google.gson.annotations.SerializedName

data class POJOVideoCdnTS(

    @field:SerializedName("per_page")
	val perPage: Int? = null,

    @field:SerializedName("data")
	val data: List<DataItem?>? = null,

    @field:SerializedName("last_page")
	val lastPage: Int? = null,

    @field:SerializedName("next_page_url")
	val nextPageUrl: String? = null,

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

data class Translation(

	@field:SerializedName("short_title")
	val shortTitle: String? = null,

	@field:SerializedName("shorter_title")
	val shorterTitle: String? = null,

	@field:SerializedName("smart_title")
	val smartTitle: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("source_quality")
	val sourceQuality: String? = null,

	@field:SerializedName("iframe_src")
	val iframeSrc: String? = null,

	@field:SerializedName("iframe")
	val iframe: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("priority")
	val priority: Int? = null,

	@field:SerializedName("max_quality")
	val maxQuality: Int? = null,

	@field:SerializedName("episodes_count")
	val episodesCount: Int? = null
)

data class TranslationsItem(

	@field:SerializedName("short_title")
	val shortTitle: String? = null,

	@field:SerializedName("shorter_title")
	val shorterTitle: String? = null,

	@field:SerializedName("smart_title")
	val smartTitle: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("source_quality")
	val sourceQuality: String? = null,

	@field:SerializedName("iframe_src")
	val iframeSrc: String? = null,

	@field:SerializedName("iframe")
	val iframe: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("priority")
	val priority: Int? = null,

	@field:SerializedName("max_quality")
	val maxQuality: Int? = null,

	@field:SerializedName("episodes_count")
	val episodesCount: Int? = null
)

data class DataItem(

    @field:SerializedName("end_date")
	val endDate: Any? = null,

    @field:SerializedName("episode_count")
	val episodeCount: Int? = null,

    @field:SerializedName("orig_title")
	val origTitle: String? = null,

    @field:SerializedName("imdb_id")
	val imdbId: String? = null,

    @field:SerializedName("kinopoisk_id")
	val kinopoiskId: String? = null,

    @field:SerializedName("content_id")
	val contentId: Any? = null,

    @field:SerializedName("preview_iframe_src")
	val previewIframeSrc: String? = null,

    @field:SerializedName("created")
	val created: String? = null,

    @field:SerializedName("last_episode_id")
	val lastEpisodeId: Int? = null,

    @field:SerializedName("iframe_src")
	val iframeSrc: String? = null,

    @field:SerializedName("ru_title")
	val ruTitle: String? = null,

    @field:SerializedName("blocked")
	val blocked: Int? = null,

    @field:SerializedName("content_type")
	val contentType: String? = null,

    @field:SerializedName("translations")
	val translations: List<TranslationsItem?>? = null,

    @field:SerializedName("id")
	val id: Int? = null,

    @field:SerializedName("iframe")
	val iframe: String? = null,

    @field:SerializedName("updated")
	val updated: String? = null,

    @field:SerializedName("season_count")
	val seasonCount: Int? = null,

    @field:SerializedName("country_id")
	val countryId: Any? = null,

    @field:SerializedName("episodes")
	val episodes: List<EpisodesItem?>? = null,

    @field:SerializedName("start_date")
	val startDate: String? = null
)

data class MediaItem(

	@field:SerializedName("translation_id")
	val translationId: Int? = null,

	@field:SerializedName("content_id")
	val contentId: Int? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("accepted")
	val accepted: String? = null,

	@field:SerializedName("max_quality")
	val maxQuality: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("duration")
	val duration: Int? = null,

	@field:SerializedName("tv_series_id")
	val tvSeriesId: Int? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("content_type")
	val contentType: String? = null,

	@field:SerializedName("blocked")
	val blocked: Int? = null,

	@field:SerializedName("translation")
	val translation: Translation? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("source_quality")
	val sourceQuality: String? = null
)

data class EpisodesItem(

    @field:SerializedName("orig_title")
	val origTitle: String? = null,

    @field:SerializedName("imdb_id")
	val imdbId: Any? = null,

    @field:SerializedName("kinopoisk_id")
	val kinopoiskId: String? = null,

    @field:SerializedName("created")
	val created: String? = null,

    @field:SerializedName("num")
	val num: String? = null,

    @field:SerializedName("ru_released")
	val ruReleased: String? = null,

    @field:SerializedName("season_id")
	val seasonId: Int? = null,

    @field:SerializedName("season_num")
	val seasonNum: Int? = null,

    @field:SerializedName("media")
	val media: List<MediaItem?>? = null,

    @field:SerializedName("tv_series_id")
	val tvSeriesId: Int? = null,

    @field:SerializedName("ru_title")
	val ruTitle: String? = null,

    @field:SerializedName("id")
	val id: Int? = null,

    @field:SerializedName("released")
	val released: String? = null
)
