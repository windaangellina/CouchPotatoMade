package com.winda.couchpotato.core.data.source.remote.api.response.search


import com.google.gson.annotations.SerializedName
import com.winda.couchpotato.core.di.CoreModule
import com.winda.couchpotato.core.utils.FunctionLibrary

class SearchResultMovieResponse(
    var adult: Boolean,
    @SerializedName("backdrop_path")
    var backdropPath: String?,
    @SerializedName("genre_ids")
    var genreIds: List<Int>,
    var id: Int,
    @SerializedName("original_language")
    var originalLanguage: String,
    @SerializedName("original_title")
    var originalTitle: String,
    var overview: String,
    var popularity: Double,
    @SerializedName("poster_path")
    var posterPath: String?,
    @SerializedName("release_date")
    var releaseDate: String?,
    var title: String,
    var video: Boolean,
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
            if (releaseDate != null){
                releaseDate.let { dateStr ->
                    releaseDateEpoch = (dateStr?.let { FunctionLibrary.getDateAsLong(it) }
                        ?: return@let)
                }
            }
        }.getOrNull()
        return releaseDateEpoch
    }
}