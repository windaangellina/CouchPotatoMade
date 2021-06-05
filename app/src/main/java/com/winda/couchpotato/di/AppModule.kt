package com.winda.couchpotato.di

import com.winda.couchpotato.core.data.viewmodel.ShowsViewModel
import com.winda.couchpotato.core.domain.usecase.MovieDatabaseInteractor
import com.winda.couchpotato.core.domain.usecase.MovieDatabaseUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val useCaseModule = module {

        factory<MovieDatabaseUseCase> { MovieDatabaseInteractor(movieDatabaseRepository = get()) }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    val viewModelModule = module {
        viewModel { ShowsViewModel(movieDatabaseUseCase = get()) }
    }
}