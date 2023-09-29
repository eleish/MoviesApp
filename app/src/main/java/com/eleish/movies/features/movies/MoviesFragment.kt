package com.eleish.movies.features.movies

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.eleish.entities.Movie
import com.eleish.movies.core.BindingFragment
import com.eleish.movies.core.addOnBottomReachedListener
import com.eleish.movies.core.isNetworkAvailable
import com.eleish.movies.core.showLongToast
import com.eleish.movies.R
import com.eleish.movies.databinding.FragmentMoviesBinding
import com.eleish.yassirtask.features.movies.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : BindingFragment<FragmentMoviesBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMoviesBinding
        get() = FragmentMoviesBinding::inflate

    private val viewModel by viewModels<MoviesViewModel>()
    private val moviesAdapter = MoviesAdapter(::onMovieClicked)

    private val connectivityManager by lazy {
        context?.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    }
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var wasPreviouslyConnected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wasPreviouslyConnected = context?.isNetworkAvailable() ?: false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSwipeRefresh()
        observeData()
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

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loading.collect {
                        binding.loadingPb.isVisible = it
                    }
                }

                launch {
                    viewModel.movies.collect {
                        moviesAdapter.submitList(it)
                    }
                }

                launch {
                    viewModel.error.collect {
                        val message = it ?: getString(R.string.something_went_wrong)
                        context?.showLongToast(message)
                    }
                }
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

    private fun monitorNetworkAvailability() {

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if (wasPreviouslyConnected) {
                    return
                }
                context?.showLongToast(R.string.network_restored)
                viewModel.fetchMovies()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                context?.showLongToast(R.string.no_internet_connection)
                wasPreviouslyConnected = false
            }
        }.also {
            connectivityManager.registerNetworkCallback(networkRequest, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        networkCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
        }
    }

    override fun onResume() {
        super.onResume()
        monitorNetworkAvailability()
    }
}