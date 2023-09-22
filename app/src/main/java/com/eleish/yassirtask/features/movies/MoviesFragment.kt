package com.eleish.yassirtask.features.movies

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.eleish.entities.Movie
import com.eleish.entities.PosterSize
import com.eleish.yassirtask.R
import com.eleish.yassirtask.core.BindingFragment
import com.eleish.yassirtask.core.isNetworkAvailable
import com.eleish.yassirtask.core.showLongToast
import com.eleish.yassirtask.databinding.FragmentMoviesBinding
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

        setupRecyclerView(emptyList())
        setupSwipeRefresh()
        observeData()
    }

    private fun setupRecyclerView(movies: List<Movie>) {
        binding.moviesComposeView.setContent {
            LazyColumn(Modifier.fillMaxSize()) {
                items(movies, key = { it.id }) {
                    MovieItem(movie = it, onClick = ::onMovieClicked)
                }
            }
        }
//        binding.moviesRv.addOnBottomReachedListener {
//            Log.d(MoviesFragment::class.java.simpleName, "End of RV reached")
//            viewModel.fetchMovies()
//        }
//        binding.moviesRv.adapter = moviesAdapter
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
                        setupRecyclerView(it)
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

@Composable
fun MovieItem(movie: Movie, modifier: Modifier = Modifier, onClick: (Movie) -> Unit) {
    Surface(onClick = {
        onClick.invoke(movie)
    }) {
        Card(
            shape = RoundedCornerShape(2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(8.dp)
        ) {
            Row {
                AsyncImage(
                    model = movie.getPosterUrl(PosterSize.MEDIUM),
                    contentDescription = "Movie poster",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f / 1.5f)
                )
                Column {
                    Text(
                        text = movie.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )
                    Text(
                        text = movie.releaseYear.toString(),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Text(text = "Rating goes here", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}