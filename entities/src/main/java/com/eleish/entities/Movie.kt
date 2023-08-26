package com.eleish.entities

import java.io.Serializable

data class Movie(
    val id: Int,
    val title: String,
    val releaseYear: Int,
    val rating: Float,
    val overview: String,
    val posterUrl: String
): Serializable
