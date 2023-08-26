package com.eleish.yassirtask.features.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eleish.entities.Movie
import com.eleish.yassirtask.R
import com.eleish.yassirtask.core.BindingFragment
import com.eleish.yassirtask.core.addOnBottomReachedListener
import com.eleish.yassirtask.core.showLongToast
import com.eleish.yassirtask.databinding.FragmentMoviesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : BindingFragment<FragmentMoviesBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMoviesBinding
        get() = FragmentMoviesBinding::inflate

    private val viewModel by viewModels<MoviesViewModel>()
    private val moviesAdapter = MoviesAdapter(::onMovieClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()
        setupRecyclerView()
        setupSwipeRefresh()
    }

    private fun setupRecyclerView() {
        binding.moviesRv.addOnBottomReachedListener {
            Log.d(MoviesFragment::class.java.simpleName, "End of RV reached")
            viewModel.fetchMovies()
        }
        binding.moviesRv.adapter = moviesAdapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.clearMovies()
            viewModel.fetchMovies()
        }
    }

    private fun observeLiveData() {
        with(viewModel) {
            loading.observe(viewLifecycleOwner) {
                binding.loadingPb.isVisible = it
            }
            error.observe(viewLifecycleOwner) {
                val message = it ?: getString(R.string.something_went_wrong)
                context?.showLongToast(message)
            }
            movies.observe(viewLifecycleOwner) {
                moviesAdapter.submitList(it)
            }
        }
    }

    private fun onMovieClicked(movie: Movie) {
        findNavController().navigate(
            MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(
                movie
            )
        )
    }
}