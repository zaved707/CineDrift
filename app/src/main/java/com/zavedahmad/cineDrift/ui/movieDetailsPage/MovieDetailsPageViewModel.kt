package com.zavedahmad.cineDrift.ui.movieDetailsPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.cineDrift.MovieDetailPageRoute
import com.zavedahmad.cineDrift.movieDetailModel.MovieDetailModel
import com.zavedahmad.cineDrift.retrofitApi.MovieDetailApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = MovieDetailsPageViewModel.Factory::class)
class MovieDetailsPageViewModel @AssistedInject constructor(
    @Assisted val navKey: MovieDetailPageRoute,
    val movieApi: MovieDetailApi
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: MovieDetailPageRoute): MovieDetailsPageViewModel
    }


    private val  _movie =  MutableStateFlow<MovieDetailModel?>(null)
    val movie = _movie.asStateFlow()
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    val authToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxYzAxMmM3MTRkNzQxYTFjODI2MTQwNWRlY2I0NGUxZCIsIm5iZiI6MTc0Mzk0ODYzOS44ODIsInN1YiI6IjY3ZjI4YjVmZTFkNWMyM2M2ZWQ5NTc3YyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.Pi_yYhz9wJV7f-6qzcY6hBczzLbmk0etPBdQ9_NL3D0"
    init {
        println("viewModel Created")
        fetchData()
    }

    override fun onCleared() {
        println("details Page ViewModel Deleted")
    }
    fun fetchData(){
        viewModelScope.launch (Dispatchers.IO){
            try {
                _isLoading.value = true
                _error.value = null // Clear previous errors
                val response = movieApi.getMovie(
                    movieId = navKey.id,
                    language = "en-US",
                    authHeader = "Bearer $authToken"
                )
                if (response.isSuccessful) {
                    _movie.value = response.body()
                } else {
                    _error.value = "Failed to fetch movie: ${response.code()}"
                    println("Cannot find movie: ${response.code()}")
                }
            } catch (e: Exception) {
                _error.value = "Failed to fetch movie. Please try again."
                println("Error: ${e.message}")}
            finally {
                _isLoading.value = false
            }
        }
    }
}

