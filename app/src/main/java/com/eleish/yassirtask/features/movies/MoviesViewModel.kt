package com.eleish.yassirtask.features.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eleish.data.MoviesRepositoryImpl
import com.eleish.domain.usecases.GetMoviesUseCase
import com.eleish.entities.Movie
import com.eleish.entities.None
import com.eleish.entities.Result
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val getMoviesUseCase: GetMoviesUseCase = GetMoviesUseCase(MoviesRepositoryImpl())
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            _loading.postValue(true)
            val result = getMoviesUseCase.invoke(None())
            _loading.postValue(false)

            when (result) {
                is Result.Failure -> _error.postValue(result.exception.message)
                is Result.Success -> {
                    val allMovies = movies.value?.toMutableList()?.apply {
                        addAll(result.data)
                    } ?: result.data

                    _movies.postValue(allMovies)
                }
            }
        }
    }
}