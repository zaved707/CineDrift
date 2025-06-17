package com.zavedahmad.cineDrift.ui.movieDetailsPage

import android.graphics.Paint
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import com.valentinilk.shimmer.shimmer
import com.zavedahmad.cineDrift.Screen

//import com.zavedahmad.cineDrift.roomDatabase.FavouritesDao

private val modifier = Modifier

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
fun MovieDetailsPage(viewModel: MovieDetailsPageViewModel, backStack: SnapshotStateList<NavKey>) {

    val movie = viewModel.movie.collectAsStateWithLifecycle().value
    val error by viewModel.error.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isFavourite by viewModel.isFavourite.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = {
        1
    })
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Details") },
            actions = {
                Row {
                    when (isFavourite) {
                        true -> {
                            IconButton(
                                onClick = { viewModel.deleteFromFavourite() },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = "Unlike"
                                )
                            }
                        }

                        false -> {
                            IconButton(
                                onClick = { viewModel.addToFavourite() },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Icon(
                                    Icons.Default.FavoriteBorder,
                                    contentDescription = "like"
                                )
                            }
                        }

                        else -> {
                            LoadingIndicator()
                        }


                    }
                    IconButton(onClick = { backStack.add(Screen.SettingsPageRoute) }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(

                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ), scrollBehavior = scrollBehavior
        )
    }) { innerPadding ->
        if (error == "NoApi") {
            PullToRefreshBox(
                modifier = Modifier
                    .padding()
                    .fillMaxSize(),
                isRefreshing = isLoading,
                onRefresh = { viewModel.reloadFromScreen() }
            ) {
                VerticalPager(state = pagerState) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.KeyOff,
                            modifier = Modifier.size(200.dp),
                            contentDescription = "placeholderr",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text("Either you Api is Not set Or is Invalid")
                        Button(onClick = { backStack.add(Screen.SettingsPageRoute) }) {
                            Text("Go to settings to change it")
                        }
                    }
                }
            }
        } else if (error != null) {
            PullToRefreshBox(
                modifier = Modifier
                    .padding()
                    .fillMaxSize(),
                isRefreshing = isLoading,
                onRefresh = { viewModel.reloadFromScreen() }
            ) {
                VerticalPager(state = pagerState) {
                    Column(


                        modifier = Modifier.fillMaxSize(),

                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(error.toString())

                    }
                }
            }


        } else {
            if (isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingIndicator()
                }
            } else if (movie == null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Search,
                        modifier = Modifier.size(200.dp),
                        contentDescription = "placeholderr",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                }
            }  else {
            val imageLink = "https://image.tmdb.org/t/p/original${movie.poster_path}"
            val title = movie.title
            val movieDescription = movie.overview
            val painter =
                rememberAsyncImagePainter(imageLink)
            val state = painter.state.collectAsStateWithLifecycle()
            val scrollState = rememberScrollState()


            Column(
                modifier = modifier
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
            ) {

                    Box(
                        modifier

                            .height(400.dp)

                    ) {
                        when (state.value) {
                            is AsyncImagePainter.State.Empty -> {
                                Text("Empty")
                            }

                            is AsyncImagePainter.State.Loading -> {
                                Box(
                                    modifier = modifier
                                        .fillMaxSize()
                                        .shimmer()
                                        .background(MaterialTheme.colorScheme.tertiary),
                                    contentAlignment = Alignment.Center
                                ) {

                                }
                            }

                            is AsyncImagePainter.State.Success -> {
                                Image(
                                    modifier = modifier.fillMaxSize(),
                                    painter = painter,
                                    contentDescription = "duck"
                                )
                            }

                            else -> { Column(
                                modifier = modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.Error,
                                    modifier = modifier.size(200.dp),
                                    contentDescription = "placeholderr",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text("No Image Was Found!!", fontSize = 30.sp, textAlign = TextAlign.Center)

                            }
                            }
                        }
                    }
                    Column(modifier.fillMaxSize()) {
                        Text(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            text = title,
                            fontSize = 40.sp,
                            style = TextStyle(lineHeight = 40.sp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Left
                        )
                        Text(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(20.dp), text = movieDescription

                        )
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween
                        ) {


                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceBright,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) { Text("Share") }
                        }
                    }
                }
            }
        }
    }
}