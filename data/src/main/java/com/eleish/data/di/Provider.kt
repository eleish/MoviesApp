package com.eleish.data.di

import com.eleish.data.core.network.OkHttpClient
import com.eleish.data.core.network.RetroFitClient

internal object Provider {

    val retrofit by lazy {
        RetroFitClient.newInstance("https://api.themoviedb.org/3/", OkHttpClient.newInstance())
    }
}