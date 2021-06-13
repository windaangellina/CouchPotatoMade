package com.winda.couchpotato.core.utils

import com.winda.couchpotato.core.data.source.local.entity.FavoriteShowEntity
import com.winda.couchpotato.core.data.source.local.entity.ShowEntity
import com.winda.couchpotato.core.data.source.remote.api.response.search.SearchMovieResponse
import com.winda.couchpotato.core.data.source.remote.api.response.search.SearchTvShowsResponse
import com.winda.couchpotato.core.domain.model.Show
import kotlin.math.roundToInt

object DataMapper {
    fun mapFavoriteEntitiesToDomain(input: List<FavoriteShowEntity>): List<Show> =
        input.map {
            Show(
                showId = it.id,
                posterUrl = it.posterUrl,
                backdropUrl = it.backdropUrl,
                title = it.title,
                releaseDateEpoch = it.releaseDateEpoch,
                userScores = it.userScores,
                overview = it.overview
            )
        }

    fun mapShowEntitiesToDomain(input : List<ShowEntity>) : List<Show> =
        input.map {
            Show(
                showId = it.id,
                posterUrl = it.posterUrl,
                backdropUrl = it.backdropUrl,
                title = it.title,
                releaseDateEpoch = it.releaseDateEpoch,
                userScores = it.userScores,
                overview = it.overview
            )
        }


    fun mapDomainToFavoriteEntity(input: Show) = FavoriteShowEntity(
        id = input.showId,
        posterUrl = input.posterUrl,
        backdropUrl = input.backdropUrl,
        title = input.title,
        releaseDateEpoch = input.releaseDateEpoch,
        userScores = input.userScores,
        overview = input.overview
    )

    fun mapListDomainToShowEntities(inputListShow: List<Show>, type : String) : List<ShowEntity> =
        inputListShow.map { input ->
            ShowEntity(
                id = input.showId,
                type = type,
                posterUrl = input.posterUrl,
                backdropUrl = input.backdropUrl,
                title = input.title,
                releaseDateEpoch = input.releaseDateEpoch,
                userScores = input.userScores,
                overview = input.overview
            )
        }

    fun mapSearchMovieResponseToShowEntities(input : SearchMovieResponse) :
            List<ShowEntity> = input.searchResultMovieResponses.map { resultDetail ->
            ShowEntity(
                resultDetail.id,
                ShowEntity.TYPE_MOVIES,
                resultDetail.getPosterUrl(),
                resultDetail.getBackdropUrl(),
                resultDetail.title,
                resultDetail.getReleaseDateEpoch(),
                (resultDetail.voteAverage * 10).roundToInt(),
                resultDetail.overview
            )
        }

    fun mapSearchTvResponseToShowEntities(input : SearchTvShowsResponse) :
            List<ShowEntity> = input.searchResultTvShowResponses.map { resultDetail ->
            ShowEntity(
                resultDetail.id,
                ShowEntity.TYPE_TV,
                resultDetail.getPosterUrl(),
                resultDetail.getBackdropUrl(),
                resultDetail.name,
                resultDetail.getReleaseDateEpoch(),
                (resultDetail.voteAverage * 10).roundToInt(),
                resultDetail.overview
            )
        }
}