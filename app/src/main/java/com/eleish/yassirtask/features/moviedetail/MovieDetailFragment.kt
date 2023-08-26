package com.eleish.yassirtask.features.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import com.eleish.entities.PosterSize
import com.eleish.yassirtask.core.BindingFragment
import com.eleish.yassirtask.databinding.FragmentMovieDetailBinding

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
            overviewTv.text = movie.overview
            moviePosterIv.load(movie.getPosterUrl(PosterSize.ORIGINAL))
        }
    }
}