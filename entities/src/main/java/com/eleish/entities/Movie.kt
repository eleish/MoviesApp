package com.eleish.entities

import java.io.Serializable

data class Movie(
    val id: Int,
    val title: String,
    val releaseYear: Int,
    val rating: Float,
    val overview: String,
    private val posterBaseUrl: String,
    private val posterPath: String
) : Serializable {

    fun getPosterUrl(posterSize: PosterSize): String {
        return "$posterBaseUrl${posterSize.code}$posterPath"
    }
}
