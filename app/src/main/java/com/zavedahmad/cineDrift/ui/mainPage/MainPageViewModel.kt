package com.zavedahmad.cineDrift.ui.mainPage


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.cineDrift.AuthorizationModel
import com.zavedahmad.cineDrift.popularMoviesModel.MoviesResponse
import com.zavedahmad.cineDrift.retrofitApi.MovieDetailApi
import com.zavedahmad.cineDrift.roomDatabase.PreferencesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel()
class MainPageViewModel @Inject constructor(
    val movieDetailApi: MovieDetailApi,
    val preferencesDao: PreferencesDao
) :
    ViewModel() {
    override fun onCleared() {
        println("mainViewModelCleared")
    }

    private val _authToken = MutableStateFlow<String>("")
    val authToken = _authToken.asStateFlow()


    private val _topRatedMovies = MutableStateFlow<MoviesResponse?>(null)
    val topRatedMovies = _topRatedMovies.asStateFlow()
    private val _popularMovies = MutableStateFlow<MoviesResponse?>(null)
    val popularMovies = _popularMovies.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()


    private val _isAuthenticated = MutableStateFlow<AuthorizationModel?>(null)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    init {
        setApiKeyAndFetchData()

    }

    fun setApiKeyAndFetchData() {
        viewModelScope.launch() {
            _authToken.value = preferencesDao.getPreference("ApiKey")?.value ?: "empty"
            try {
                _isLoading.value = true
                _error.value = null
                _isAuthenticated.value = null
                println("authentication set to null")
                val authenticatedResponse =
                    movieDetailApi.verifyKey(authHeader = "Bearer ${_authToken.value}")
                if (authenticatedResponse.isSuccessful) {
                    _isAuthenticated.value = authenticatedResponse.body()
                }
            } catch (e: Exception) {
                _error.value = "NoApi"
                println("Error: ${e.message}")


            }
            if (_isAuthenticated.value?.success?: false) {
                fetchData()
            } else {
                _isLoading.value = false
                _error.value = "NoApi"
            }
        }
    }

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                _isLoading.value = true
                _error.value = null
                val popularMoviesResponse =
                    movieDetailApi.getPopularMovies(
                        page = 1,
                        authHeader = "Bearer ${authToken.value}", listOf = "popular"
                    )
                if (popularMoviesResponse.isSuccessful) {
                    _popularMovies.value = popularMoviesResponse.body()
                }
                val topRatedrMoviesResponse =
                    movieDetailApi.getPopularMovies(
                        page = 1,
                        authHeader = "Bearer ${authToken.value}", listOf = "top_rated"
                    )
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