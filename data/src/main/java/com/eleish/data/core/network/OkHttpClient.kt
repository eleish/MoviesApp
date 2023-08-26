package com.eleish.data.core.network

import android.util.Log
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpClient {
    // TODO: Remove after implementing auth
    private const val TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxYjgyZTg1NGI3NTQ1MzZlZTMxN2Q2NjI0N2E0NDNjMCIsInN1YiI6IjY0ZTljYzY1MDZmOTg0MDE0ZTY4YjM3MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.MHpNjdcblxuki2vhzgzkH7gmb-f_drGluGNWz8G8UGY"

    fun newInstance(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d(
                "OkHttpClient",
                "API Request -> $message"
            )
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .authenticator(object : Authenticator {
                override fun authenticate(route: Route?, response: Response): Request? {
                    if (response.request.header("Authorization") != null)
                        return null

                    return response.request.newBuilder().header("Authorization", "Bearer $TOKEN")
                        .build()
                }
            })

        return okHttpClientBuilder.build()
    }
}