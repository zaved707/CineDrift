package com.zavedahmad.cineDrift.ui.searchScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.cineDrift.AuthorizationModel
import com.zavedahmad.cineDrift.popularMoviesModel.MoviesResponse
import com.zavedahmad.cineDrift.retrofitApi.MovieDetailApi
import com.zavedahmad.cineDrift.roomDatabase.PreferencesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class SSViewModel @Inject constructor(
    val preferencesDao: PreferencesDao, val movieDetailApi: MovieDetailApi
) : ViewModel() {
    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    private val _movies = MutableStateFlow<MoviesResponse?>(null)
    val movies = _movies.asStateFlow()
    private val _authToken = MutableStateFlow<String>("")
    val authToken = _authToken.asStateFlow()
    private val _isAuthenticated = MutableStateFlow<AuthorizationModel?>(null)
    val isAuthenticated = _isAuthenticated.asStateFlow()
    private val _tries = MutableStateFlow(0)
    fun changeSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery
    }

    suspend fun checkApiAuth() {
        try {


            _isLoading.value = true
            _error.value = null
            _isAuthenticated.value = null


            val authenticatedResponse =
                movieDetailApi.verifyKey(authHeader = "Bearer ${_authToken.value}")
            println(
                "inside checkAuth authenticatedResponse: $authenticatedResponse " + "\n body ${authenticatedResponse.body()}, " + "\n authenticatedResponse.isSuccessful : ${authenticatedResponse.isSuccessful}"
            )
            if (authenticatedResponse.isSuccessful) {
                _isAuthenticated.value = authenticatedResponse.body()

            } else {
                println("came Inside Unsuccessfull")
                _error.value = "NoApi"
            }

        } catch (e: Exception) {
            _error.value = "println Cannot authorize Api"
            _isAuthenticated.value = AuthorizationModel(true)


        } finally {

        }
    }

    fun reloadFromScreen() {
        _isAuthenticated.value = AuthorizationModel(true)
        _tries.value = 0
        setApiKeyAndFetchData()
    }

    fun setApiKeyAndFetchData() {

        viewModelScope.launch(Dispatchers.IO) {

            _authToken.value = preferencesDao.getPreference("ApiKey")?.value ?: "empty"

            if (_isAuthenticated.value?.success ?: false) {

                fetchData()
            } else {

                _isLoading.value = false
                _error.value = "NoApi"
            }
        }

    }

    suspend fun fetchData() {

        try {

            _isLoading.value = true
            _error.value = null // Clear previous errors
            val response = movieDetailApi.getMoviesBySearch(
                query = _searchQuery.value,
                includeAdult = false,
                page = 1,
                language = "en-US",
                authHeader = "Bearer ${authToken.value}"
            )
            if (response.isSuccessful) {
                _movies.value = response.body()
                _isLoading.value = false

            } else {
                if (_tries.value <= 1) {
                    _tries.value += 1

                    checkApiAuth()
                    setApiKeyAndFetchData()


                } else {
                    _tries.value = 0
                    _error.value = "Failed to fetch movie. Please try again. tries: ${_tries.value}"
                    _isLoading.value = false
                }

            }

        } catch (e: Exception) {
            if (_tries.value <= 1) {
                _tries.value += 1

                checkApiAuth()
                setApiKeyAndFetchData()


            } else {

                _error.value =
                    "Failed to fetch movie. Please try again. ${e.message} tries: ${_tries.value}"
                _tries.value = 0
                _isLoading.value = false
            }
        }
    }
}