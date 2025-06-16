package com.zavedahmad.cineDrift.ui.searchScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.cineDrift.ui.components.MyBottomBar
import com.zavedahmad.cineDrift.ui.components.MyTopABCommon
import com.zavedahmad.cineDrift.ui.components.SmallMovieCard

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

            MyTopABCommon(backStack,scrollBehavior,"Search")
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
                .padding(top = 30.dp, start = 30.dp, end = 30.dp, bottom = 10.dp)
               ,
            value = query,
            onValueChange = { viewModel.changeSearchQuery(it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                viewModel.setApiKeyAndFetchData()
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
                Column(modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp)) {
                    LazyColumn {
                        items(movies.results) { item ->
                            SmallMovieCard(backStack, item)
                        }

                    }
                }
            }
        }
    }}
}