package com.example.nav3recipes.ui.mainPage


import androidx.lifecycle.ViewModel
import com.example.nav3recipes.MainPageRoute
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = MainPageViewModel.Factory::class)
class MainPageViewModel @AssistedInject constructor(@Assisted val navKey: MainPageRoute) :
    ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(navKey: MainPageRoute): MainPageViewModel
    }
}