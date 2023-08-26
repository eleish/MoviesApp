package com.eleish.data.core.network

import android.util.Log
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

internal object OkHttpClient {

    fun newInstance(authToken: String): OkHttpClient {
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

                    return response.request.newBuilder()
                        .header("Authorization", "Bearer $authToken")
                        .build()
                }
            })

        return okHttpClientBuilder.build()
    }
}