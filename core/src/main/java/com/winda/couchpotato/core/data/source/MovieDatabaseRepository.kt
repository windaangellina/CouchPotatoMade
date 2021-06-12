package com.winda.couchpotato.core.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.winda.couchpotato.core.data.network.NetworkBoundResource
import com.winda.couchpotato.core.data.network.Resource
import com.winda.couchpotato.core.data.source.local.LocalDataSource
import com.winda.couchpotato.core.data.source.local.entity.ShowEntity
import com.winda.couchpotato.core.data.source.remote.RemoteDataSource
import com.winda.couchpotato.core.data.source.remote.api.network.ApiResponse
import com.winda.couchpotato.core.data.source.remote.api.response.search.SearchMovieResponse
import com.winda.couchpotato.core.data.source.remote.api.response.search.SearchTvShowsResponse
import com.winda.couchpotato.core.domain.model.Show
import com.winda.couchpotato.core.domain.repository.IMovieDatabaseDataSource
import com.winda.couchpotato.core.utils.DataMapper
import com.winda.couchpotato.core.utils.FunctionLibrary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class MovieDatabaseRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource) : IMovieDatabaseDataSource {

    // status
    override val isLoading = MutableLiveData<Boolean>()
    override val responseCode = MutableLiveData<Int>()

    private val TAG = "MovieDatabaseRepository"

    override fun getSearchMoviesResult(searchKeyword: String): Flow<Resource<List<Show>>> {
        return object : NetworkBoundResource<List<Show>, SearchMovieResponse>() {
            override fun loadFromDB(): Flow<List<Show>> {
                val searchQuery = FunctionLibrary.buildLikeQuery(searchKeyword)
                return localDataSource.getListMovies(searchQuery).map {
                    DataMapper.mapShowEntitiesToDomain(it)
                }
            }
            override fun shouldFetch(data: List<Show>?): Boolean =
                data == null || data.isEmpty()
            //true

            override suspend fun createCall(): Flow<ApiResponse<SearchMovieResponse>> =
                remoteDataSource.getSearchMovieResult(searchKeyword)

            override suspend fun saveCallResult(data: SearchMovieResponse) {
                val listEntity = DataMapper.mapSearchMovieResponseToShowEntities(data)
                localDataSource.insertMovies(listEntity)
            }
        }.asFlow()
    }

    override fun getSearchTvShowsResult(searchKeyword: String): Flow<Resource<List<Show>>> {
        return object : NetworkBoundResource<List<Show>, SearchTvShowsResponse>(){
            override fun loadFromDB(): Flow<List<Show>> {
                val searchQuery = FunctionLibrary.buildLikeQuery(searchKeyword)
                val result = localDataSource.getListTvShows(searchKeyword = searchQuery)
                return result.map {
                    DataMapper.mapShowEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean =
                data == null || data.isEmpty()
            //true

            override suspend fun createCall(): Flow<ApiResponse<SearchTvShowsResponse>> =
                remoteDataSource.getSearchTvShowResult(searchKeyword)

            override suspend fun saveCallResult(data: SearchTvShowsResponse) {
                val listEntity = DataMapper.mapSearchTvResponseToShowEntities(data)
                if (listEntity.isEmpty()){
                    Log.d(TAG, "size saveCallResult tv shows : empty")
                }
                else{
                    localDataSource.insertTvShows(listEntity)
                    Log.d(TAG, "size saveCallResult tv shows : ${listEntity.size}")
                }

            }
        }.asFlow()
    }

    override fun getListFavorite(): Flow<List<Show>> {
        return localDataSource.getListFavorite().map { DataMapper.mapFavoriteEntitiesToDomain(it) }
    }

    override fun getCountFavoriteById(id: Int): LiveData<Int> {
        return localDataSource.getCountFavoriteById(id)
    }

    override fun getListMovies(searchKeyword: String): Flow<List<Show>> {
        return localDataSource.getListMovies(searchKeyword).map {
            DataMapper.mapShowEntitiesToDomain(it)
        }
    }

    override fun getListTvShows(searchKeyword: String): Flow<List<Show>> {
        return localDataSource.getListTvShows(searchKeyword).map {
            DataMapper.mapShowEntitiesToDomain(it)
        }
    }

    override suspend fun insertFavorite(show: Show) {
        val showEntity = DataMapper.mapDomainToFavoriteEntity(show)
        localDataSource.insertFavorite(showEntity)
    }

    override suspend fun deleteFavorite(show: Show) {
        val showEntity = DataMapper.mapDomainToFavoriteEntity(show)
        localDataSource.deleteFavorite(showEntity)
    }

    override suspend fun insertMovies(listMovies: List<Show>) {
        val listEntity = DataMapper.mapListDomainToShowEntities(listMovies, ShowEntity.TYPE_MOVIES)
        localDataSource.insertMovies(listEntity)
    }

    override suspend fun insertTvShows(listTvShows: List<Show>) {
        val listEntity = DataMapper.mapListDomainToShowEntities(listTvShows, ShowEntity.TYPE_TV)
        localDataSource.insertTvShows(listEntity)
    }

}