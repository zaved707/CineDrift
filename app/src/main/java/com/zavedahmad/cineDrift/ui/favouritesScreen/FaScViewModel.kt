package com.zavedahmad.cineDrift.ui.favouritesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.cineDrift.roomDatabase.FavouritesDao
import com.zavedahmad.cineDrift.roomDatabase.FavouritesEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FaScViewModel @Inject constructor(val favouritesDao: FavouritesDao) : ViewModel()  {

    private  val _movies = MutableStateFlow<List<FavouritesEntity>?>(null)
    val movies = _movies.asStateFlow()

    init {
        viewModelScope.launch(context = Dispatchers.IO) {
            favouritesDao.getAllFavouritesFlow().collect { favouritesList ->
                _movies.value = favouritesList
            }
        }
    }


}