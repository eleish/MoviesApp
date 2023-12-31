package com.eleish.movies.features.moviedetail

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import com.eleish.entities.PosterSize
import com.eleish.movies.core.BindingFragment
import com.eleish.movies.databinding.FragmentMovieDetailBinding

class MovieDetailFragment : BindingFragment<FragmentMovieDetailBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMovieDetailBinding
        get() = FragmentMovieDetailBinding::inflate

    private val args by navArgs<MovieDetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateUI()
    }

    private fun populateUI() {
        val movie = args.movie

        with(binding) {
            movieTitleTv.text = movie.title
            releaseYearTv.text = movie.releaseYear.toString()
            ratingRb.rating = movie.rating
            overviewTv.apply {
                text = movie.overview
                movementMethod = ScrollingMovementMethod()
            }
            moviePosterIv.load(movie.getPosterUrl(PosterSize.ORIGINAL))
        }
    }
}