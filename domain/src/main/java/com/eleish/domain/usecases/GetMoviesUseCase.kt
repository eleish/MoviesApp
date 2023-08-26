package com.eleish.domain.usecases

import com.eleish.domain.core.UseCase
import com.eleish.domain.repositories.MoviesRepository
import com.eleish.entities.Movie
import com.eleish.entities.MoviesPage
import com.eleish.entities.None
import com.eleish.entities.Result

class GetMoviesUseCase(private val repository: MoviesRepository) :
    UseCase<GetMoviesParams, Result<List<Movie>>>() {
    override suspend fun invoke(params: GetMoviesParams): Result<MoviesPage> {
        return repository.getMovies(params.page)
    }
}

data class GetMoviesParams(val page: Int)