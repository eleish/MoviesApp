package com.eleish.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eleish.data.datasources.MoviesRemoteDataSource
import com.eleish.data.models.toMovie
import com.eleish.domain.repositories.MoviesRepository
import com.eleish.entities.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class MoviesRepositoryImpl @Inject constructor(
    private val moviesRemoteDataSource: MoviesRemoteDataSource
) : MoviesRepository {

    override fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                MoviesPagingSource()
            }
        ).flow
    }

    inner class MoviesPagingSource : PagingSource<Int, Movie>() {
        override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
            // TODO: Understand
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
            return try {
                val page = params.key ?: 1
                val result = moviesRemoteDataSource.fetchMovies(page)
                val movies = result.results.map { it.toMovie() }
                LoadResult.Page(
                    data = movies,
                    prevKey = if (page == 1) null else page.minus(1),
                    nextKey = if (movies.isEmpty()) page else page.plus(1)
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }
}