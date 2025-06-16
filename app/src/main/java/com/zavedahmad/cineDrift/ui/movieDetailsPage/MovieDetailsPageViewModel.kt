package com.zavedahmad.cineDrift.ui.movieDetailsPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.zavedahmad.cineDrift.Screen.MovieDetailPageRoute
import com.zavedahmad.cineDrift.movieDetailModel.MovieDetailModel
import com.zavedahmad.cineDrift.retrofitApi.MovieDetailApi
import com.zavedahmad.cineDrift.roomDatabase.PreferencesDao
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
    val movieApi: MovieDetailApi,
    val preferencesDao: PreferencesDao
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
    private val  _authToken = MutableStateFlow<String>("")
    val authToken = _authToken.asStateFlow()
    init {
        println("viewModel Created")
        setApiKeyAndFetchData()
    }

    override fun onCleared() {
        println("details Page ViewModel Deleted")
    }
    fun setApiKeyAndFetchData(){
        viewModelScope.launch(){
            _authToken.value = preferencesDao.getPreference("ApiKey")?.value?:  "empty"
            if (_authToken.value != "empty") {
                fetchData()
            } else {
                _isLoading.value=false
                _error.value = "API key is empty"
            }
        }

    }
    fun fetchData(){
        viewModelScope.launch (Dispatchers.IO){
            try {
                _isLoading.value = true
                _error.value = null // Clear previous errors
                val response = movieApi.getMovie(
                    movieId = navKey.id,
                    language = "en-US",
                    authHeader = "Bearer ${authToken.value}"
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

