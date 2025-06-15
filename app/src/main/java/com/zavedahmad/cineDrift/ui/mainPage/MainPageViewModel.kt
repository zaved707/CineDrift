package com.zavedahmad.cineDrift.ui.mainPage


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.cineDrift.popularMoviesModel.MoviesCombined
import com.zavedahmad.cineDrift.popularMoviesModel.MoviesResponse
import com.zavedahmad.cineDrift.retrofitApi.MovieDetailApi
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel()
class MainPageViewModel @Inject constructor(val movieDetailApi: MovieDetailApi) :
    ViewModel() {
    override fun onCleared() {
        println("mainViewModelCleared")
    }

    val authToken =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxYzAxMmM3MTRkNzQxYTFjODI2MTQwNWRlY2I0NGUxZCIsIm5iZiI6MTc0Mzk0ODYzOS44ODIsInN1YiI6IjY3ZjI4YjVmZTFkNWMyM2M2ZWQ5NTc3YyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.Pi_yYhz9wJV7f-6qzcY6hBczzLbmk0etPBdQ9_NL3D0"


    private val _topRatedMovies = MutableStateFlow<MoviesResponse?>(null)
    val topRatedMoviess = _topRatedMovies.asStateFlow()
    private val _popularMovies = MutableStateFlow<MoviesResponse?>(null)
    val popularMovies = _popularMovies.asStateFlow()
    private val _movies = MutableStateFlow<MoviesCombined?>(null)
    val movies = _movies.asStateFlow()
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()


    init {
        fetchData()
    }
    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                _error.value = null
                val popularMoviesResponse =
                    movieDetailApi.getPopularMovies(page = 1,
                        authHeader = "Bearer $authToken", listOf = "popular")
                if (popularMoviesResponse.isSuccessful) {
                     _popularMovies.value = popularMoviesResponse.body()
                }
                val topRatedrMoviesResponse =
                    movieDetailApi.getPopularMovies(page = 1,
                        authHeader = "Bearer $authToken", listOf = "top_rated")
                if (topRatedrMoviesResponse.isSuccessful) {
                    _topRatedMovies.value = topRatedrMoviesResponse.body()
                }

            } catch (e: Exception) {
                _error.value = "Failed to fetch movie. Please try again. ${e.message}"
                println("Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }

        }
        println("mainViewModelCreated")
    }
}