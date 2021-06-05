package com.winda.couchpotato.core.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.winda.couchpotato.core.data.network.Resource
import com.winda.couchpotato.core.domain.model.Show
import kotlinx.coroutines.flow.Flow

interface IMovieDatabaseDataSource {
    // status
    val isLoading : MutableLiveData<Boolean>
    val responseCode : MutableLiveData<Int>

    // API
    fun getSearchMoviesResult(searchKeyword : String) : Flow<Resource<List<Show>>>
    fun getSearchTvShowsResult(searchKeyword: String) : Flow<Resource<List<Show>>>

    // local
    fun getListFavorite() : Flow<List<Show>>
    fun getCountFavoriteById(id:Int) : LiveData<Int>
    fun getListMovies(searchKeyword: String) : Flow<List<Show>>
    fun getListTvShows(searchKeyword: String) : Flow<List<Show>>
    suspend fun insertFavorite(show: Show)
    suspend fun deleteFavorite(show: Show)
    suspend fun insertTvShows(listTvShows : List<Show>)
    suspend fun insertMovies(listMovies : List<Show>)
}