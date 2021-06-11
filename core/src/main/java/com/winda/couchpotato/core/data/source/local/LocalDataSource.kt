package com.winda.couchpotato.core.data.source.local

import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import com.winda.couchpotato.core.data.source.local.entity.FavoriteShowEntity
import com.winda.couchpotato.core.data.source.local.entity.ShowEntity
import com.winda.couchpotato.core.data.source.local.room.MovieDatabaseDao
import kotlinx.coroutines.flow.Flow

@Keep
open class LocalDataSource(private val movieDatabaseDao: MovieDatabaseDao) {

    fun getListFavorite() : Flow<List<FavoriteShowEntity>> {
        return movieDatabaseDao.getListFavorite()
    }

    fun getListMovies(searchKeyword : String) : Flow<List<ShowEntity>> {
        return movieDatabaseDao.getShows(ShowEntity.TYPE_MOVIES, searchKeyword)
    }

    fun getListTvShows(searchKeyword: String) : Flow<List<ShowEntity>> {
        return movieDatabaseDao.getShows(ShowEntity.TYPE_TV, searchKeyword)
    }

    fun getCountFavoriteById(id : Int) : LiveData<Int> {
        return movieDatabaseDao.getCountFavoriteById(id)
    }

    suspend fun insertFavorite(favoriteShowEntity: FavoriteShowEntity){
        movieDatabaseDao.insertFavorite(favoriteShowEntity)
    }

    suspend fun deleteFavorite(favoriteShowEntity: FavoriteShowEntity){
        movieDatabaseDao.deleteFavorite(favoriteShowEntity)
    }

    suspend fun insertMovies(listMovies : List<ShowEntity>) {
        movieDatabaseDao.insertMovies(listMovies)
    }

    suspend fun insertTvShows(listShows : List<ShowEntity>) {
        movieDatabaseDao.insertTvShows(listShows)
    }
}