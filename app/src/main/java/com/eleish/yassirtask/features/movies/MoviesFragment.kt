package com.eleish.yassirtask.features.movies

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.eleish.entities.Movie
import com.eleish.entities.PosterSize
import com.eleish.yassirtask.R
import com.eleish.yassirtask.core.showLongToast
import com.eleish.yassirtask.features.compose.components.connectivityState
import com.eleish.yassirtask.features.compose.components.pulltorefresh.PullRefreshIndicator
import com.eleish.yassirtask.features.compose.components.pulltorefresh.pullRefresh
import com.eleish.yassirtask.features.compose.components.pulltorefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun MoviesScreen(viewModel: MoviesViewModel = viewModel(), onNavigateToDetails: (Movie) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var refreshing by remember {
        mutableStateOf(false)
    }

    val loading by viewModel.loading.collectAsStateWithLifecycle(initialValue = false)
    val movies by viewModel.movies.collectAsStateWithLifecycle(initialValue = emptyList())
    val connected by connectivityState()

    val refreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = {
        refreshing = true
        viewModel.clearMovies()
        viewModel.fetchMovies()
        coroutineScope.launch {
            delay(500)
            refreshing = false
        }
    })

    Box(modifier = Modifier.pullRefresh(state = refreshState)) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(movies, key = { it.id }) {
                MovieItem(movie = it) { movie ->
                    onNavigateToDetails.invoke(movie)
                }
            }
        }

        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = refreshing,
            state = refreshState
        )

        if (loading)
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )

        if (connected.not()) {
            Surface(color = Color.Red, modifier = Modifier.align(Alignment.BottomCenter)) {
                Text(
                    text = context.getString(R.string.no_internet_connection),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.White
                )
            }
        }

        LaunchedEffect(Unit) {
            viewModel.error.collectLatest {
                context.showLongToast(it ?: context.getString(R.string.something_went_wrong))
            }
        }
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