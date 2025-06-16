package com.zavedahmad.cineDrift.ui.favouritesScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.cineDrift.Screen
import com.zavedahmad.cineDrift.ui.components.MyBottomBar
import com.zavedahmad.cineDrift.ui.components.MyTopABCommon
import com.zavedahmad.cineDrift.ui.components.SmallMovieCard

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FavouritesScreen(
    backStack: SnapshotStateList<NavKey>,
    viewModel: FaScViewModel
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {

            MyBottomBar(backStack)

        }, topBar = {

            MyTopABCommon(backStack,scrollBehavior,"Favourites")

        }) { innerPadding ->
        val movies by viewModel.movies.collectAsStateWithLifecycle()
        val scrollState = rememberScrollState()


        Column(modifier = Modifier.padding(innerPadding)) {


            movies?.let { list ->
                if (list.isNotEmpty()) {
                    LazyColumn {
                        items(list) { item ->
                            SmallMovieCard(backStack, item.movieData)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

            }


        }
    }


}