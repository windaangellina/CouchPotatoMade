package com.winda.couchpotato.core.domain.usecase

import androidx.lifecycle.LiveData
import com.winda.couchpotato.core.data.network.Resource
import com.winda.couchpotato.core.data.source.MovieDatabaseRepository
import com.winda.couchpotato.core.domain.model.Show
import com.winda.couchpotato.core.domain.repository.IMovieDatabaseDataSource
import kotlinx.coroutines.flow.Flow

class MovieDatabaseInteractor
    (private val movieDatabaseRepository: IMovieDatabaseDataSource) : MovieDatabaseUseCase {

    override val isLoading : LiveData<Boolean> = movieDatabaseRepository.isLoading
    override val responseCode : LiveData<Int> = movieDatabaseRepository.responseCode

    override fun getSearchMoviesResult(searchKeyword: String): Flow<Resource<List<Show>>> {
        return movieDatabaseRepository.getSearchMoviesResult(searchKeyword)
    }

    override fun getSearchTvShowsResult(searchKeyword: String): Flow<Resource<List<Show>>> {
        return movieDatabaseRepository.getSearchTvShowsResult(searchKeyword)
    }

    override fun getListFavorite(): Flow<List<Show>> {
        return movieDatabaseRepository.getListFavorite()
    }

    override fun getCountFavoriteById(id: Int): LiveData<Int> {
        return movieDatabaseRepository.getCountFavoriteById(id)
    }

    override fun getListMovies(searchKeyword: String): Flow<List<Show>> {
        return movieDatabaseRepository.getListMovies(searchKeyword)
    }

    override fun getListTvShows(searchKeyword: String): Flow<List<Show>> {
        return movieDatabaseRepository.getListTvShows(searchKeyword)
    }

    override suspend fun insertFavorite(show: Show) {
        movieDatabaseRepository.insertFavorite(show)
    }

    override suspend fun deleteFavorite(show: Show) {
        movieDatabaseRepository.deleteFavorite(show)
    }

    override suspend fun insertMovies(listMovies: List<Show>) {
        movieDatabaseRepository.insertMovies(listMovies)
    }

    override suspend fun insertTvShows(listTvShows: List<Show>) {
        movieDatabaseRepository.insertTvShows(listTvShows)
    }
}