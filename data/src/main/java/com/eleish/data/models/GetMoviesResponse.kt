package com.eleish.data.models

import com.google.gson.annotations.SerializedName

internal data class GetMoviesResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieModel>
)
