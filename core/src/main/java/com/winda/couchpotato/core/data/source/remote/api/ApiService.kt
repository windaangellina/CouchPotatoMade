package com.winda.couchpotato.core.data.source.remote.api

import com.winda.couchpotato.core.data.source.remote.api.response.search.SearchMovieResponse
import com.winda.couchpotato.core.data.source.remote.api.response.search.SearchTvShowsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey:String,
        @Query("query") searchKeyword:String
    ) : SearchMovieResponse


    // https://api.themoviedb.org/3/search/tv?api_key=0e0818b611e7dfb71928a75cd059f740&query=walking dead
    @GET("search/tv")
    suspend fun searchTvShows(
        @Query("api_key") apiKey:String,
        @Query("query") searchKeyword:String
    ) : SearchTvShowsResponse
}