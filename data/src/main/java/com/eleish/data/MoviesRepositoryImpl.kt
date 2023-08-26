package com.eleish.data

import com.eleish.domain.repositories.MoviesRepository
import com.eleish.entities.Movie
import com.eleish.entities.Result
import kotlinx.coroutines.delay

class MoviesRepositoryImpl : MoviesRepository {
    override suspend fun getMovies(): Result<List<Movie>> {
        delay(3000)
        return Result.Success(
            listOf(
                Movie(
                    id = 0,
                    name = "Shawshank Redemption",
                    releaseYear = 1996,
                    rating = 4.9f,
                    posterUrl = ""
                ),
                Movie(
                    id = 1,
                    name = "The Green Mile",
                    releaseYear = 1996,
                    rating = 4.5f,
                    posterUrl = ""
                ),
                Movie(
                    id = 3,
                    name = "Shawshank Redemption",
                    releaseYear = 1996,
                    rating = 4.9f,
                    posterUrl = ""
                ),
                Movie(
                    id = 4,
                    name = "The Green Mile",
                    releaseYear = 1996,
                    rating = 4.5f,
                    posterUrl = ""
                ),
                Movie(
                    id = 5,
                    name = "Shawshank Redemption",
                    releaseYear = 1996,
                    rating = 4.9f,
                    posterUrl = ""
                ),
                Movie(
                    id = 6,
                    name = "The Green Mile",
                    releaseYear = 1996,
                    rating = 4.5f,
                    posterUrl = ""
                )
            )
        )
    }

    override suspend fun getMovie(id: Int): Result<Movie> {
        TODO("Not yet implemented")
    }
}