package com.eleish.yassirtask.features.movies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.eleish.domain.usecases.GetMoviesParams
import com.eleish.domain.usecases.GetMoviesUseCase
import com.eleish.entities.Movie
import com.eleish.entities.Result
import com.eleish.yassirtask.core.observeNetworkAvailabilityAsFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    application: Application,
    private val getMoviesUseCase: GetMoviesUseCase
) : AndroidViewModel(application) {

    private val _error = MutableSharedFlow<String?>()
    val error: Flow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: Flow<Boolean> = _loading

    private val _movies = MutableStateFlow(emptyList<Movie>())
    val movies: Flow<List<Movie>> = _movies

    private var page = 1

    init {
        viewModelScope.launch {
            application.observeNetworkAvailabilityAsFlow().collectLatest {
                if (it) {
                    fetchMovies()
                }
            }
        }

        fetchMovies()
    }

    fun clearMovies() {
        page = 1
        viewModelScope.launch {
            _movies.emit(emptyList())
        }
    }

    fun fetchMovies() {
        if (_loading.value)
            return

        viewModelScope.launch {
            _loading.emit(true)
            delay(500)

            when (val result = getMoviesUseCase.invoke(GetMoviesParams(page))) {
                is Result.Failure -> {
                    _error.emit(result.exception.message)
                }

                is Result.Success -> {
                    page = result.data.page + 1

                    val allMovies = _movies.value.toMutableList().apply {
                        addAll(result.data.movies)
                    }

                    _movies.emit(allMovies)
                }
            }

            _loading.emit(false)
        }
    }
}