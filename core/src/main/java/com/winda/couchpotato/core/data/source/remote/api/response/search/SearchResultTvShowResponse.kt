package com.winda.couchpotato.core.data.source.remote.api.response.search


import com.google.gson.annotations.SerializedName
import com.winda.couchpotato.core.di.CoreModule
import com.winda.couchpotato.core.utils.FunctionLibrary


class SearchResultTvShowResponse(
    @SerializedName("backdrop_path")
    var backdropPath: String,
    @SerializedName("first_air_date")
    var firstAirDate: String?,
    @SerializedName("genre_ids")
    var genreIds: List<Int>,
    var id: Int,
    var name: String,
    @SerializedName("origin_country")
    var originCountry: List<String>,
    @SerializedName("original_language")
    var originalLanguage: String,
    @SerializedName("original_name")
    var originalName: String,
    var overview: String,
    var popularity: Double,
    @SerializedName("poster_path")
    var posterPath: String,
    @SerializedName("vote_average")
    var voteAverage: Double,
    @SerializedName("vote_count")
    var voteCount: Int
) {
    fun getPosterUrl() : String{
        return CoreModule.BASE_URL_POSTER + posterPath
    }

    fun getBackdropUrl() : String{
        return CoreModule.BASE_URL_BACKDROP + backdropPath
    }

    fun getReleaseDateEpoch() : Long{
        var releaseDateEpoch : Long = 0
        kotlin.runCatching {
            if (firstAirDate != null){
                firstAirDate.let { dateStr ->
                    releaseDateEpoch = (dateStr?.let { FunctionLibrary.getDateAsLong(it) }
                        ?: return@let)
                }
            }
        }.getOrNull()
        return releaseDateEpoch
    }
}