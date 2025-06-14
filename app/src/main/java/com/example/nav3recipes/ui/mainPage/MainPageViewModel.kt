package com.example.nav3recipes.ui.mainPage


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nav3recipes.popularMoviesModel.MoviesResponse
import com.example.nav3recipes.retrofitApi.MovieDetailApi
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

    private val _movies = MutableStateFlow<MoviesResponse?>(null)
    val movies = _movies.asStateFlow()
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                _error.value = null
                val response =
                    movieDetailApi.getPopularMovies(page = 1,
                        authHeader = "Bearer $authToken", listOf = "top_rated")
                if (response.isSuccessful) {
                    _movies.value = response.body()
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