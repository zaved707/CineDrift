package com.zavedahmad.cineDrift.ui.mainPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
    val topRatedMovies = viewModel.topRatedMovies.collectAsStateWithLifecycle().value
    val pagerState = rememberPagerState(pageCount = {
        1
    })

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        bottomBar = { MyBottomBar(backStack) },
        topBar = { MyTopABCommon(backStack, scrollBehavior, "CineDrift") }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            isRefreshing = isLoading,
            onRefresh = { viewModel.reloadFromScreen() }
        ) {


            if (error == "NoApi") {
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
            } else if (error != null) {
                VerticalPager(state = pagerState) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(error.toString())

                    }
                }


            } else {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxSize()
                ) {

                    Column(Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            "Popular Movies",
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp)
                        )

                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                        if (isLoading) ShimmerBlocksMain()
                        else if (popularMovies == null) Box(
                            Modifier.fillMaxSize(),
                            Alignment.Center
                        ) { Text("Empty") }
                        else MovieCarousel(backStack, popularMovies)

                    Spacer(Modifier.height(20.dp))
                    Column(Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            "Top-Rated Movies",
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp)
                        )
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                        if (isLoading) ShimmerBlocksMain()
                        else if (topRatedMovies == null) Box(
                            Modifier.fillMaxSize(),
                            Alignment.Center
                        ) { Text("Empty") }
                        else MovieCarousel(backStack, topRatedMovies)

                }
            }
        }
    }
}