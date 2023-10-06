package com.eleish.movies.features.movies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eleish.domain.usecases.GetMoviesUseCase
import com.eleish.entities.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    application: Application,
    private val getMoviesUseCase: GetMoviesUseCase
) : AndroidViewModel(application) {

    val movies: Flow<PagingData<Movie>> = getMoviesUseCase.invoke().cachedIn(viewModelScope)
}