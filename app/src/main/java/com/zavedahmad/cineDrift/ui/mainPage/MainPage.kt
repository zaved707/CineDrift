package com.zavedahmad.cineDrift.ui.mainPage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.cineDrift.Screen
import com.zavedahmad.cineDrift.ui.components.MovieCarousel
import com.zavedahmad.cineDrift.ui.components.MyBottomBar
import com.zavedahmad.cineDrift.ui.components.MyTopABCommon
import com.zavedahmad.cineDrift.ui.components.ShimmerBlocksMain

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainPage(backStack: SnapshotStateList<NavKey>, viewModel: MainPageViewModel) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val popularMovies = viewModel.popularMovies.collectAsStateWithLifecycle().value
    val topRatedMovies = viewModel.topRatedMoviess.collectAsStateWithLifecycle().value


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = { MyBottomBar(backStack) },
        topBar = { MyTopABCommon(backStack, scrollBehavior, "CineDrift") }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier.padding(innerPadding),
            isRefreshing = isLoading,
            onRefresh = { viewModel.setApiKeyAndFetchData() }
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
            ) {


                if (error == "NoApi") {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Button(onClick = { backStack.add(Screen.SettingsPageRoute) }) {
                            Text("go to settings to change it")
                        }
                    }
                } else if (error != null) {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text(error.toString())
                    }
                } else {
                    Column(Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            "Popular Movies",
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp)
                        )
                        HorizontalDivider()
                        if (isLoading) ShimmerBlocksMain()
                        else if (popularMovies == null) Box(Modifier.fillMaxSize(), Alignment.Center) { Text("Empty") }
                        else MovieCarousel(backStack, popularMovies)
                    }
                    Spacer(Modifier.height(20.dp))
                    Column(Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            "Top-Rated Movies",
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp)
                        )
                        HorizontalDivider()
                        if (isLoading) ShimmerBlocksMain()
                        else if (topRatedMovies == null) Box(Modifier.fillMaxSize(), Alignment.Center) { Text("Empty") }
                        else MovieCarousel(backStack, topRatedMovies)
                    }
                }
            }
        }
    }
}