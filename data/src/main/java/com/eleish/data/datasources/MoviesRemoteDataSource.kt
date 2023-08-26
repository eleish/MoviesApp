package com.eleish.data.datasources

import com.eleish.data.models.GetMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface MoviesRemoteDataSource {
    @GET("discover/movie")
    suspend fun fetchMovies(@Query("page") page: Int): GetMoviesResponse
}