package com.eleish.data.repositories

import com.eleish.data.datasources.MoviesRemoteDataSource
import com.eleish.data.models.toMovie
import com.eleish.domain.repositories.MoviesRepository
import com.eleish.entities.MoviesPage
import com.eleish.entities.Result
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesRemoteDataSource: MoviesRemoteDataSource
) : MoviesRepository {
    override suspend fun getMovies(page: Int): Result<MoviesPage> {
        return try {
            val result = moviesRemoteDataSource.fetchMovies(page)
            val movies = result.results.map { it.toMovie() }
            Result.Success(MoviesPage(result.page, movies))
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}