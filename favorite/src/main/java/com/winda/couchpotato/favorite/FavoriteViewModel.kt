package com.winda.couchpotato.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.winda.couchpotato.core.domain.model.Show
import com.winda.couchpotato.core.domain.usecase.MovieDatabaseUseCase

class FavoriteViewModel(private val movieDatabaseUseCase: MovieDatabaseUseCase) : ViewModel() {
    fun getListFavorite() : LiveData<List<Show>> {
        return movieDatabaseUseCase.getListFavorite().asLiveData()
    }
}