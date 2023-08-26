package com.eleish.data.core.di

import com.eleish.data.core.network.RetroFitClient
import com.eleish.data.datasources.MoviesRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton
import com.eleish.data.core.network.OkHttpClient as OkHttpClientCreator

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Singleton
    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClientCreator.newInstance(Constants.TOKEN)
    }

    @Singleton
    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        return RetroFitClient.newInstance(Constants.BASE_URL, okHttpClient)
    }

    @Singleton
    @Provides
    fun provideMoviesDataSource(retrofit: Retrofit): MoviesRemoteDataSource {
        return retrofit.create(MoviesRemoteDataSource::class.java)
    }
}