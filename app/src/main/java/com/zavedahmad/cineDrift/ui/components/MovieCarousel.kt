package com.zavedahmad.cineDrift.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.zavedahmad.cineDrift.Screen.MovieDetailPageRoute
import com.zavedahmad.cineDrift.popularMoviesModel.MoviesResponse

//horizontal movie corousal with a title

@Composable
fun MovieCarousel(backStack:  SnapshotStateList<NavKey>,movies: MoviesResponse){
    LazyRow {
        items(movies.results) { item ->
            val imageLink = "https://image.tmdb.org/t/p/w185${item.poster_path}"
            val painter =
                rememberAsyncImagePainter(imageLink)
            val state = painter.state.collectAsStateWithLifecycle()
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Card(modifier = Modifier.width(200.dp), onClick = {backStack.add((MovieDetailPageRoute(item.id.toString())))}) {
                    Box(
                        modifier = Modifier.size(width = 200.dp, height = 280.dp)
                            .padding(20.dp), contentAlignment = Alignment.Center
                    ) {

                        when (state.value) {
                            is AsyncImagePainter.State.Empty -> {
                                Text("Empty")
                            }

                            is AsyncImagePainter.State.Loading -> {

                                CircularProgressIndicator()

                            }

                            is AsyncImagePainter.State.Success -> {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painter,
                                    contentDescription = "duck"
                                )
                            }

                            else -> {
                                Text(state.value.toString())
                            }
                        }

                    }


                    Column(modifier = Modifier.fillMaxWidth().height(80.dp).padding(10.dp)) {
                        Text(item.title)

                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
            }

        }
    }
}