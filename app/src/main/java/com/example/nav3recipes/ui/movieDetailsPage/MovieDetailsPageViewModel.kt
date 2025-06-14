package com.example.nav3recipes.ui.movieDetailsPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nav3recipes.MovieDetailPageRoute
import com.example.nav3recipes.movieDetailModel.MovieDetailModel
import com.example.nav3recipes.retrofitApi.MovieDetailApi

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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

    fun fetchData(){
        viewModelScope.launch {
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
            } catch (e: UnknownHostException) {
                _error.value = "No internet connection. Please check your network."
                println("No internet: ${e.message}")

            } catch (e: SocketTimeoutException) {
                _error.value = "Request timed out. Please try again."
                println("Timeout: ${e.message}")
            } catch (e: IOException) {
                _error.value = "Network error occurred. Please try again."
                println("Network error: ${e.message}")
            } catch (e: Exception) {
                _error.value = "Unexpected error: ${e.message}"
                println("Unexpected error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

