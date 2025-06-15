package com.zavedahmad.cineDrift.ui.searchScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.zavedahmad.cineDrift.Screen.MovieDetailPageRoute
import com.zavedahmad.cineDrift.ui.components.MyBottomBar

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(backStack :  SnapshotStateList<NavKey>, viewModel: SSViewModel) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val query = viewModel.searchQuery.collectAsStateWithLifecycle().value
    val movies = viewModel.movies.collectAsStateWithLifecycle().value
    Scaffold (modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {

            MyBottomBar(backStack)

    },topBar = {

            TopAppBar(
                title = { Text("Search") },

                scrollBehavior = scrollBehavior

            )
    }){  innerPadding ->
    Column (modifier = Modifier.padding(innerPadding)){
        OutlinedTextField(
            leadingIcon = {
                Icon(
                    Icons.Default.Search, contentDescription = "search for movie you want "
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top= 30.dp, start = 30.dp, end= 30.dp, bottom = 10.dp )
               ,
            value = query,
            onValueChange = { viewModel.changeSearchQuery(it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                viewModel.fetchData()
            }),
            placeholder = {
                Row {

                    Text("Search")
                }
            })

        if (error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(error.toString())
            }
        } else {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingIndicator()
                }
            } else if (movies == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Empty")
                }
            } else {
                Column(modifier = Modifier.padding(horizontal = 20.dp).padding(top = 10.dp)) {
                    LazyColumn {
                        items(movies.results) { item ->
                            val imageLink = "https://image.tmdb.org/t/p/w154${item.poster_path}"
                            val painter =
                                rememberAsyncImagePainter(imageLink)
                            val state = painter.state.collectAsStateWithLifecycle()
                            OutlinedCard(onClick = {backStack.add(MovieDetailPageRoute(item.id.toString()))},
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
                                            .background(MaterialTheme.colorScheme.inverseSurface)

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
                                        Text(item.title)
                                    }

                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }

                    }
                }
            }
        }
    }}
}