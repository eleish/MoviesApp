package com.eleish.data.models

import com.eleish.data.core.di.Constants
import com.eleish.entities.Movie
import com.google.gson.annotations.SerializedName
import java.util.Calendar
import java.util.Date

internal data class MovieModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("release_date")
    val releaseDate: Date,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String
)

internal fun MovieModel.toMovie(): Movie {
    val releaseYear = Calendar.getInstance().apply {
        time = releaseDate
    }.get(Calendar.YEAR)
    val rating = voteAverage / 2f // To convert from 10 to 5 stars
    return Movie(
        id = id,
        title = title,
        releaseYear = releaseYear,
        rating = rating,
        overview = overview,
        posterBaseUrl = Constants.POSTER_BASE_URL,
        posterPath = "/$posterPath"
    )
}
