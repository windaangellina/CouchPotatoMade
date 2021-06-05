package com.winda.couchpotato.favorite.di

import com.winda.couchpotato.favorite.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteModule = module{
    viewModel { FavoriteViewModel(get()) }
}