package com.winda.couchpotato.core.data.source.remote

import android.util.Log
import com.winda.couchpotato.core.data.source.remote.api.ApiService
import com.winda.couchpotato.core.data.source.remote.api.network.ApiResponse
import com.winda.couchpotato.core.data.source.remote.api.response.search.SearchMovieResponse
import com.winda.couchpotato.core.data.source.remote.api.response.search.SearchTvShowsResponse
import com.winda.couchpotato.core.di.CoreModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

open class RemoteDataSource(private val apiService: ApiService) {
    companion object{
        private const val TAG = "RemoteDataSource"
        private const val API_KEY = CoreModule.API_KEY
    }

    suspend fun getSearchMovieResult(searchKeyword: String) : Flow<ApiResponse<SearchMovieResponse>>{
        //get data from remote api
        return flow {
            try {
                val response = apiService.searchMovies(API_KEY, searchKeyword)
                val dataArray = response.searchResultMovieResponses
                if (dataArray.isNotEmpty()){
                    emit(ApiResponse.Success(response))
                    Log.d(TAG, "size response JsonHelper movies : ${dataArray.size}")
                } else {
                    emit(ApiResponse.Empty)
                    Log.d(TAG, "empty response JsonHelper movies")
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e(TAG, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSearchTvShowResult(searchKeyword: String) : Flow<ApiResponse<SearchTvShowsResponse>>{
        //get data from remote api
        return flow {
            try {
                val response = apiService.searchTvShows(API_KEY, searchKeyword)
                val dataArray = response.searchResultTvShowResponses
                if (dataArray.isNotEmpty()){
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e(TAG, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}