package com.eleish.domain.usecases

import com.eleish.domain.core.UseCase
import com.eleish.domain.repositories.MoviesRepository
import com.eleish.entities.Movie
import com.eleish.entities.None
import com.eleish.entities.Result

class GetMoviesUseCase(private val repository: MoviesRepository) :
    UseCase<None, Result<List<Movie>>>() {
    override suspend fun invoke(params: None): Result<List<Movie>> {
        return repository.getMovies()
    }
}