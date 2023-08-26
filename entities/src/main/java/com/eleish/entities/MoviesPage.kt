package com.eleish.entities

data class MoviesPage(
    val page: Int,
    val movies: List<Movie>
)
