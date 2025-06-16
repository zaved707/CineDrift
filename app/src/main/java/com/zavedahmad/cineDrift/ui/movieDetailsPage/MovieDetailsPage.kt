package com.zavedahmad.cineDrift.ui.movieDetailsPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.zavedahmad.cineDrift.roomDatabase.FavouritesDao
import com.zavedahmad.cineDrift.ui.components.MyTopABCommon

//import com.zavedahmad.cineDrift.roomDatabase.FavouritesDao

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MovieDetailsPage (viewModel: MovieDetailsPageViewModel,backStack: SnapshotStateList<NavKey>) {

    val movie = viewModel.movie.collectAsStateWithLifecycle().value
    val error by viewModel.error.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    if (error !=null){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(error.toString())}
    }else{
    if (isLoading){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        CircularProgressIndicator()}
    }
    else if ( movie == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            CircularProgressIndicator()}
    } else {
        val imageLink = "https://image.tmdb.org/t/p/original${movie.poster_path}"
        val title = movie.title
        val movieDescription =movie.overview
        val painter =
            rememberAsyncImagePainter(imageLink)
        val state = painter.state.collectAsStateWithLifecycle()
        val scrollState = rememberScrollState()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        Scaffold(topBar = {MyTopABCommon(backStack, scrollBehavior,"Movie Details")}) { innerPadding->
        Column(modifier = Modifier.verticalScroll(scrollState).padding(innerPadding)) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(MaterialTheme.colorScheme.inverseSurface)
            ) {
                when (state.value) {
                    is AsyncImagePainter.State.Empty -> {
                        Text("Empty")
                    }

                    is AsyncImagePainter.State.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                            CircularProgressIndicator()}
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
            Column(Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    text = title,
                    fontSize = 40.sp,
                    style = TextStyle(lineHeight = 40.sp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp), text = movieDescription

                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {viewModel.addToFavourite()},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) { Text("Add to favourites") }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceBright,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) { Text("Share") }
                }
            }
        }}
    }}
}