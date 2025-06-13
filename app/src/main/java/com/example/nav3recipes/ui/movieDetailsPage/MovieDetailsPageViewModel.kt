package com.example.nav3recipes.ui.movieDetailsPage

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nav3recipes.MovieDetailPageRoute
import com.example.nav3recipes.retrofitApi.RetrofitInstance
import com.example.nav3recipes.retrofitApi.models.MovieDetailModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = MovieDetailsPageViewModel.Factory::class)
class MovieDetailsPageViewModel @AssistedInject constructor(
    @Assisted val navKey: MovieDetailPageRoute
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: MovieDetailPageRoute): MovieDetailsPageViewModel
    }
    val movieApi = RetrofitInstance.movieDetailApi

    private val  _movie =  MutableStateFlow<MovieDetailModel?>(null)
    val movie = _movie.asStateFlow()
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        println("viewModel Created")
        fetchData()
    }

    fun fetchData(){
        viewModelScope.launch {
            try {


                val response = movieApi.getMovie(
                    movieId = navKey.id,
                    language = "en-US",
                    authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxYzAxMmM3MTRkNzQxYTFjODI2MTQwNWRlY2I0NGUxZCIsIm5iZiI6MTc0Mzk0ODYzOS44ODIsInN1YiI6IjY3ZjI4YjVmZTFkNWMyM2M2ZWQ5NTc3YyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.Pi_yYhz9wJV7f-6qzcY6hBczzLbmk0etPBdQ9_NL3D0"
                )
                if (response.isSuccessful) {
                    _movie.value = response.body()

                } else {
                    println("cannot find movie")
                }
            }finally {
                _isLoading.value=  false
            } }

    }
    override fun onCleared() {
        println("viewModel cleared nigga")
    }
}

