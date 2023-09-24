package com.eleish.yassirtask.features.moviedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.eleish.entities.Movie
import com.eleish.entities.PosterSize
import com.eleish.yassirtask.features.compose.theme.BlackSemiTransparent


@Composable
fun MovieDetailsScreen(movie: Movie) {
    Box {
        AsyncImage(
            model = movie.getPosterUrl(PosterSize.ORIGINAL),
            contentDescription = "Movie Poster",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Surface(
            color = BlackSemiTransparent,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.65f)
        ) {
            Column {
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                )
                Text(
                    text = movie.releaseYear.toString(),
                    color = Color.White,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Text(
                    text = "Rating goes here",
                    color = Color.White,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                )
                Text(
                    text = movie.overview + movie.overview + movie.overview,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp)
                        .verticalScroll(rememberScrollState())
                )
            }
        }
    }
}

@Preview
@Composable
fun MovieDetailsPreview() {
    MovieDetailsScreen(
        movie = Movie(
            id = 0,
            title = "Talk to Me",
            releaseYear = 1996,
            rating = 3.8f,
            overview = "",
            posterBaseUrl = "https://image.tmdb.org/t/p/",
            posterPath = "/kdPMUMJzyYAc4roD52qavX0nLIC.jpg"
        )
    )
}