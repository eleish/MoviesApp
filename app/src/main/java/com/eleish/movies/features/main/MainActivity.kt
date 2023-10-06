package com.eleish.movies.features.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eleish.entities.Movie
import com.eleish.movies.features.compose.Routes
import com.eleish.movies.features.moviedetail.MovieDetailsScreen
import com.eleish.movies.features.movies.MoviesScreen
import com.eleish.movies.features.movies.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Routes.MOVIES) {
                composable(Routes.MOVIES) {
                    val viewModel = hiltViewModel<MoviesViewModel>()
                    MoviesScreen(viewModel = viewModel) {
                        navController.currentBackStackEntry?.savedStateHandle?.set("movie", it)
                        navController.navigate(Routes.MOVIE_DETAILS)
                    }
                }
                composable(
                    route = Routes.MOVIE_DETAILS,
                ) {
                    val movie =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Movie>("movie")
                            ?: return@composable

                    MovieDetailsScreen(movie = movie)
                }
            }
        }
    }
}