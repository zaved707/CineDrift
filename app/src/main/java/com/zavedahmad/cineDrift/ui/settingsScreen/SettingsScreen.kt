package com.zavedahmad.cineDrift.ui.settingsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(backStack: SnapshotStateList<NavKey>, viewModel: SettingsViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val userInput by viewModel.userInput.collectAsStateWithLifecycle()
    val apiKeyInDB by  viewModel.apiKeyInDB.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
         topBar = {

            TopAppBar(
                title = { Text("Settings") },

                colors = TopAppBarDefaults.topAppBarColors(

                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ), scrollBehavior = scrollBehavior
            )

        }) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Text("Enter Api ")
            TextField(value = userInput, onValueChange = { viewModel.setUserInput(it) })
            Button(onClick = {viewModel.setApiToDB()}) { Text("set Api") }
            apiKeyInDB?.let { preference ->
                Text(text = preference.value)
            } ?: Text(text = "No preference found")
        }

    }
}