package com.eleish.yassirtask.features.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eleish.domain.usecases.GetMoviesParams
import com.eleish.domain.usecases.GetMoviesUseCase
import com.eleish.entities.Movie
import com.eleish.entities.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _error = MutableSharedFlow<String?>()
    val error: Flow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: Flow<Boolean> = _loading

    private val _movies = MutableStateFlow(emptyList<Movie>())
    val movies: Flow<List<Movie>> = _movies

    private var page = 1

    init {
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