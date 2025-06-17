package com.zavedahmad.cineDrift

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.zavedahmad.cineDrift.ui.favouritesScreen.FaScViewModel
import com.zavedahmad.cineDrift.ui.favouritesScreen.FavouritesScreen
import com.zavedahmad.cineDrift.ui.mainPage.MainPage
import com.zavedahmad.cineDrift.ui.mainPage.MainPageViewModel
import com.zavedahmad.cineDrift.ui.movieDetailsPage.MovieDetailsPage
import com.zavedahmad.cineDrift.ui.movieDetailsPage.MovieDetailsPageViewModel
import com.zavedahmad.cineDrift.ui.otherPages.NoApiPage
import com.zavedahmad.cineDrift.ui.searchScreen.SSViewModel
import com.zavedahmad.cineDrift.ui.searchScreen.SearchScreen
import com.zavedahmad.cineDrift.ui.setEdgeToEdgeConfig
import com.zavedahmad.cineDrift.ui.settingsScreen.SettingsScreen
import com.zavedahmad.cineDrift.ui.settingsScreen.SettingsViewModel
import com.zavedahmad.cineDrift.ui.theme.Nav3RecipesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

sealed class Screen : NavKey {
    @Serializable
    data object NoApiPageRoute : Screen()

    @Serializable
    data object MainPageRoute : Screen()

    @Serializable
    data object SearchPageRoute : Screen()

    @Serializable
    data object FavouritePageRoute : Screen()

    @Serializable
    data object SettingsPageRoute : Screen()


    @Serializable
    data class MovieDetailPageRoute(val id: String) : Screen()
}


@AndroidEntryPoint
class RecipePickerActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEdgeToEdgeConfig()
        setContent {
            val backStack = rememberNavBackStack<Screen>(Screen.MainPageRoute)
            val viewModelFavouritesPage = hiltViewModel<FaScViewModel>()
            val viewModelMainPage = hiltViewModel<MainPageViewModel>()
            val viewModelSearchScreen = hiltViewModel<SSViewModel>()
            val isTopMainPageRoute =
                if (backStack.lastOrNull() is Screen.MainPageRoute || backStack.lastOrNull() is Screen.SearchPageRoute || backStack.lastOrNull() is Screen.FavouritePageRoute) {
                    true
                } else {
                    false
                }
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val theme by settingsViewModel.themeMode.collectAsStateWithLifecycle()
            val themeReal = theme
            if (themeReal == null) {
                Nav3RecipesTheme("system") {
                    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface))
                }
            } else {

                Nav3RecipesTheme(themeReal.value) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),

                        ) { innerPadding ->
                        var absolutePadding = PaddingValues()
                        if (isTopMainPageRoute) {
                            absolutePadding =
                                PaddingValues(bottom = innerPadding.calculateBottomPadding())
                        }

                        Box(modifier = Modifier.padding()) {


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
                                        is Screen.SearchPageRoute -> NavEntry(key = key) {
                                            SearchScreen(backStack, viewModelSearchScreen)

                                        }

                                        is Screen.MainPageRoute -> {
                                            NavEntry(key = key) {

                                                Column {

                                                    MainPage(backStack, viewModelMainPage)
                                                }
                                            }
                                        }

                                        is Screen.SettingsPageRoute -> {
                                            NavEntry(key = key) {


                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {

                                                    SettingsScreen(backStack, settingsViewModel)
                                                }
                                            }
                                        }

                                        is Screen.FavouritePageRoute -> {
                                            NavEntry(key = key) {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    FavouritesScreen(
                                                        backStack,
                                                        viewModelFavouritesPage
                                                    )
                                                }
                                            }
                                        }

                                        is Screen.NoApiPageRoute -> {
                                            NavEntry(key = key) {
                                                NoApiPage(backStack)
                                            }
                                        }

                                        is Screen.MovieDetailPageRoute -> {
                                            NavEntry(key = key) {
                                                val viewModel =
                                                    hiltViewModel<MovieDetailsPageViewModel, MovieDetailsPageViewModel.Factory>(
                                                        creationCallback = { factory ->
                                                            factory.create(key)
                                                        }
                                                    )
                                                MovieDetailsPage(viewModel, backStack)
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


}