package com.eleish.domain.repositories

import com.eleish.entities.MoviesPage
import com.eleish.entities.Result

interface MoviesRepository {

    suspend fun getMovies(page: Int): Result<MoviesPage>
}