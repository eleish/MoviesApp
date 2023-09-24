package com.eleish.domain.usecases

import androidx.paging.PagingData
import com.eleish.domain.repositories.MoviesRepository
import com.eleish.entities.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    operator fun invoke(): Flow<PagingData<Movie>> {
        return repository.getMovies()
    }
}