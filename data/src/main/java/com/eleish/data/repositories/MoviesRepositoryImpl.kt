package com.eleish.data.repositories

import com.eleish.data.datasources.MoviesRemoteDataSource
import com.eleish.data.di.Provider
import com.eleish.data.models.toMovie
import com.eleish.domain.repositories.MoviesRepository
import com.eleish.entities.Movie
import com.eleish.entities.MoviesPage
import com.eleish.entities.Result

class MoviesRepositoryImpl(
    private val moviesRemoteDataSource: MoviesRemoteDataSource = Provider.retrofit.create(
        MoviesRemoteDataSource::class.java
    )
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

    override suspend fun getMovie(id: Int): Result<Movie> {
        TODO("Not yet implemented")
    }
}