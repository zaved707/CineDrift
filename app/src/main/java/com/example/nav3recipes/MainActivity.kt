package com.example.nav3recipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.nav3recipes.ui.components.MyBottomBar
import com.example.nav3recipes.ui.mainPage.MainPage
import com.example.nav3recipes.ui.mainPage.MainPageViewModel
import com.example.nav3recipes.ui.movieDetailsPage.MovieDetailsPage
import com.example.nav3recipes.ui.movieDetailsPage.MovieDetailsPageViewModel
import com.example.nav3recipes.ui.setEdgeToEdgeConfig
import com.example.nav3recipes.ui.theme.Nav3RecipesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@Serializable
data object MainPageRoute

@Serializable
data object SearchPageRoute

@Serializable
data object FavouritePageRoute

@Serializable
data object ProfilePageRoute


@Serializable
data class MovieDetailPageRoute(val id: String)

val BottomBarItems = listOf<Any>(
    SearchPageRoute,
    MainPageRoute,
    FavouritePageRoute,
    ProfilePageRoute
)

@AndroidEntryPoint
class RecipePickerActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEdgeToEdgeConfig()
        setContent {
            val backStack = remember { mutableStateListOf<Any>(MainPageRoute) }
            val viewModelMainPage = hiltViewModel<MainPageViewModel>()
            val isTopMainPageRoute =if( backStack.lastOrNull() is MainPageRoute || backStack.lastOrNull() is SearchPageRoute || backStack.lastOrNull() is ProfilePageRoute || backStack.lastOrNull() is FavouritePageRoute  ){true}else{
                false
            }
            Nav3RecipesTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AnimatedVisibility(
                            visible = isTopMainPageRoute,
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = fadeOut()
                        ) {
                            MyBottomBar(backStack)
                        }
                    },
                    topBar = {

                        TopAppBar(
                            title = { Text("Movies") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }) { innerPadding ->
                    var absolutePadding = PaddingValues(top = innerPadding.calculateTopPadding())
                    if (isTopMainPageRoute) {
                        absolutePadding = innerPadding
                    }

                    Box(modifier = Modifier.padding(absolutePadding)) {


                        NavDisplay(
                            backStack = backStack,
                            onBack = { backStack.removeLastOrNull() },
                            entryDecorators = listOf(
                                rememberSceneSetupNavEntryDecorator(),
                                rememberSavedStateNavEntryDecorator(),
                                rememberViewModelStoreNavEntryDecorator()
                            ),
                            entryProvider = { key ->
                                when (key) {
                                    is SearchPageRoute -> NavEntry(key = key) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("Search Screen")
                                        }
                                    }

                                    is MainPageRoute -> {
                                        NavEntry(key = key) {

                                            Column {

                                                MainPage(backStack, viewModelMainPage)
                                            }
                                        }
                                    }
                                    is ProfilePageRoute->{
                                        NavEntry(key= key){ Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("Profile Screen")
                                        }}
                                    }
                                    is FavouritePageRoute->{
                                        NavEntry(key= key){ Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("Favourite Screen")
                                        }}
                                    }
                                    is MovieDetailPageRoute -> {
                                        NavEntry(key = key) {
                                            val viewModel =
                                                hiltViewModel<MovieDetailsPageViewModel, MovieDetailsPageViewModel.Factory>(
                                                    creationCallback = { factory ->
                                                        factory.create(key)
                                                    }
                                                )
                                            MovieDetailsPage(viewModel)
                                        }
                                    }

                                    else -> throw RuntimeException("Invalid NavKey.")
                                }


                            }
                        )

                    }
                }
            }
        }
    }
}


