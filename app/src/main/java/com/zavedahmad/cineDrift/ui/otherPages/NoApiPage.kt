package com.zavedahmad.cineDrift.ui.otherPages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.cineDrift.Screen
import com.zavedahmad.cineDrift.ui.components.MyBottomBar
import com.zavedahmad.cineDrift.ui.components.MyTopABCommon

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
fun NoApiPage(backStack: SnapshotStateList<NavKey>){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold (topBar = {MyTopABCommon(backStack,scrollBehavior,"No Api")} ){
        innerPadding->
        Column (modifier= Modifier.padding(innerPadding)){
            Text("YOu Have Not Entered Your API IN Settings")
            Button(onClick = {


                backStack.add(Screen.SettingsPageRoute)}) { Text("go to settings to change it")}
        }

    }
}