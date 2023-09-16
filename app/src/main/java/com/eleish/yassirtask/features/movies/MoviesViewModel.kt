package com.eleish.yassirtask.features.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eleish.domain.usecases.GetMoviesParams
import com.eleish.domain.usecases.GetMoviesUseCase
import com.eleish.entities.Movie
import com.eleish.entities.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _error = MutableSharedFlow<String?>()
    val error: Flow<String?> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private var page = 1

    init {
        fetchMovies()
    }

    fun clearMovies() {
        page = 1
        _movies.postValue(emptyList())
    }

    fun fetchMovies() {
        if (_loading.value == true)
            return

        viewModelScope.launch {
            _loading.postValue(true)

            when (val result = getMoviesUseCase.invoke(GetMoviesParams(page))) {
                is Result.Failure -> {
                    _error.emit(result.exception.message)
                }
                is Result.Success -> {
                    page = result.data.page + 1

                    val allMovies = movies.value?.toMutableList()?.apply {
                        addAll(result.data.movies)
                    } ?: result.data.movies

                    _movies.postValue(allMovies)
                }
            }

            _loading.postValue(false)
        }
    }
}