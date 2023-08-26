package com.eleish.data.di

import com.eleish.data.core.network.OkHttpClient
import com.eleish.data.core.network.RetroFitClient

internal object Provider {

    private const val TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxYjgyZTg1NGI3NTQ1MzZlZTMxN2Q2NjI0N2E0NDNjMCIsInN1YiI6IjY0ZTljYzY1MDZmOTg0MDE0ZTY4YjM3MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.MHpNjdcblxuki2vhzgzkH7gmb-f_drGluGNWz8G8UGY"


    val retrofit by lazy {
        RetroFitClient.newInstance("https://api.themoviedb.org/3/", OkHttpClient.newInstance(TOKEN))
    }
}