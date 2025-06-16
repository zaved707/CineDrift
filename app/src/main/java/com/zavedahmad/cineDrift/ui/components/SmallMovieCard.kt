package com.zavedahmad.cineDrift.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.valentinilk.shimmer.shimmer
import com.zavedahmad.cineDrift.Screen.MovieDetailPageRoute
import com.zavedahmad.cineDrift.movieDetailModel.MovieDetailModel
import com.zavedahmad.cineDrift.popularMoviesModel.Result

@Composable
fun SmallMovieCard(backStack: SnapshotStateList<NavKey>,item : Any){
    val movieID = when (item) {
        is Result -> item.id.toString()
        is MovieDetailModel -> item.id.toString()
        else -> "Unknown"
    }
    val posterPath= when (item) {
        is Result -> item.poster_path
        is MovieDetailModel -> item.poster_path
        else -> "Unknown"
    }
    val title = when (item) {
        is Result -> item.title
        is MovieDetailModel -> item.title
        else -> "Unknown"
    }
    val imageLink = "https://image.tmdb.org/t/p/w154${posterPath}"
    val painter =
        rememberAsyncImagePainter(imageLink)
    val state = painter.state.collectAsStateWithLifecycle()
    OutlinedCard(
        onClick = { backStack.add(MovieDetailPageRoute(movieID)) },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(106.5.dp)
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))


            ) {
                when (state.value) {
                    is AsyncImagePainter.State.Empty -> {
                        Text("Empty")
                    }

                    is AsyncImagePainter.State.Loading -> {

                        Box(modifier = Modifier.shimmer().fillMaxSize().background(MaterialTheme.colorScheme.tertiary))

                    }

                    is AsyncImagePainter.State.Success -> {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painter,
                            contentDescription = "duck"
                        )
                    }

                    else -> {
                        val scrollState = rememberScrollState()
                        Box(
                            modifier = Modifier.verticalScroll(scrollState),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No Image XD")
                        }
                    }
                }


            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(title)
            }

        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}