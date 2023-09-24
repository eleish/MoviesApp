package com.eleish.domain.repositories

import androidx.paging.PagingData
import com.eleish.entities.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getMovies(): Flow<PagingData<Movie>>
}