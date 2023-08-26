package com.eleish.domain.repositories

import com.eleish.entities.Movie
import com.eleish.entities.Result

interface MoviesRepository {

    suspend fun getMovies(): Result<List<Movie>>
    suspend fun getMovie(id: Int): Result<Movie>
}