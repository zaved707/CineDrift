package com.example.nav3recipes.ui.mainPage

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import com.example.nav3recipes.MovieDetailPageRoute

@Composable
fun MainPage(backStack : SnapshotStateList<Any>){
    var idValue by remember {mutableStateOf("640146")}
    Column {
        TextField(
            placeholder = {Text("enter Id")},
            value = idValue,
            onValueChange = { idValue = it })
        Button(onClick = {
            backStack.add(
                MovieDetailPageRoute(idValue)
            )
        }) { Text("hi") }
    }
}