package com.zavedahmad.cineDrift.ui.mainPage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zavedahmad.cineDrift.ui.components.MovieCarousel
import com.zavedahmad.cineDrift.ui.components.ShimmerBlocksMain

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainPage(backStack: SnapshotStateList<Any>, viewModel: MainPageViewModel) {


    val scrollState = rememberScrollState()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val popularMovies = viewModel.popularMovies.collectAsStateWithLifecycle().value
    val topRatedMovies = viewModel.topRatedMoviess.collectAsStateWithLifecycle().value

    PullToRefreshBox(isRefreshing = isLoading, onRefresh = { viewModel.fetchData() }) {
        Column(modifier = Modifier.verticalScroll(scrollState)) {


            Text(
                "Popular Movies",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp)
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))

            if (error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(error.toString())
                }
            } else {
                if (isLoading) {
                    ShimmerBlocksMain()
                } else if (popularMovies == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Empty")
                    }
                } else {

                    MovieCarousel(backStack, popularMovies)
                }
            }
            Text(
                "Top-Rated Movies",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp)
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))

            if (error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(error.toString())
                }
            } else {
                if (isLoading) {
                    ShimmerBlocksMain()
                } else if (topRatedMovies == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Empty")
                    }
                } else {
                    MovieCarousel(backStack, topRatedMovies)
                }
            }


        }
    }
}