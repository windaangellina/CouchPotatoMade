package com.winda.couchpotato.core.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.winda.couchpotato.core.data.network.Resource
import com.winda.couchpotato.core.domain.model.Show
import com.winda.couchpotato.core.domain.usecase.MovieDatabaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class ShowsViewModel(private val  movieDatabaseUseCase: MovieDatabaseUseCase) : ViewModel() {
    val loadingStatus : LiveData<Boolean> = movieDatabaseUseCase.isLoading
    val responseCode : LiveData<Int> = movieDatabaseUseCase.responseCode

    fun getSearchMovies(searchKeyword: String) : LiveData<Resource<List<Show>>> {
        return movieDatabaseUseCase.getSearchMoviesResult(searchKeyword).asLiveData()
    }

    fun getSearchTvShows(searchKeyword: String) : LiveData<Resource<List<Show>>> {
        return movieDatabaseUseCase.getSearchTvShowsResult(searchKeyword).asLiveData()
    }

    fun getCountFavoriteById(id:Int) : LiveData<Int> {
        return movieDatabaseUseCase.getCountFavoriteById(id)
    }

    fun insertFavorite(favoriteShow : Show) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDatabaseUseCase.insertFavorite(favoriteShow)
        }
    }

    fun deleteFavorite(favoriteShow: Show){
        viewModelScope.launch(Dispatchers.IO){
            movieDatabaseUseCase.deleteFavorite(favoriteShow)
        }
    }
}