package com.example.nav3recipes.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.nav3recipes.FavouritePageRoute
import com.example.nav3recipes.MainPageRoute
import com.example.nav3recipes.ProfilePageRoute
import com.example.nav3recipes.SearchPageRoute

data class ActivePageTracker(
    var homePage: Boolean,
    var profilePage: Boolean,
    var favouritesPage: Boolean,
    var searchPage: Boolean

)

val BottomBarItems = listOf<Any>(
    MainPageRoute,
    SearchPageRoute,

    FavouritePageRoute,
    ProfilePageRoute
)
val icons = listOf<ImageVector>(
    Icons.Default.Home,
    Icons.Default.Search,
    Icons.Default.FavoriteBorder,
    Icons.Default.AccountCircle

)

@Composable
fun MyBottomBar(backStack: SnapshotStateList<Any>) {

    NavigationBar {
        BottomBarItems.forEachIndexed { index, element ->

            NavigationBarItem(
                onClick = {
                    if(backStack.lastOrNull() != element){
                        if(backStack.lastOrNull() in BottomBarItems){
                            backStack.removeAt(backStack.lastIndex)
                        }
                        backStack.add(
                            element
                        )
                    }

                },
                icon = { Icon(icons[index], contentDescription = "desp") },
                selected = backStack.lastOrNull() == element
            )
        }
    }
}