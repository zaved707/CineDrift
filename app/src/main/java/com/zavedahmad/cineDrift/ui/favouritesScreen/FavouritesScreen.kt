package com.zavedahmad.cineDrift.ui.favouritesScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.cineDrift.ui.components.MyBottomBar

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FavouritesScreen(backStack: SnapshotStateList<NavKey>) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {

            MyBottomBar(backStack)

        }, topBar = {

            TopAppBar(
                title = { Text("Movies") },
                colors = TopAppBarDefaults.topAppBarColors(

                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ), scrollBehavior = scrollBehavior
            )

        }) { innerPadding ->
        Column (modifier = Modifier.padding(innerPadding)){ Text("Favourites") }
    }


}