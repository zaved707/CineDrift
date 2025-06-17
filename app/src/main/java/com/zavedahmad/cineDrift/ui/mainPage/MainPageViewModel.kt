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
    private val _tries = MutableStateFlow(0)


   private val _isAuthenticated= MutableStateFlow<AuthorizationModel?>(AuthorizationModel(true))


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

    init {
        setApiKeyAndFetchData()

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

                val topRatedrMoviesResponse =
                    movieDetailApi.getPopularMovies(
                        page = 1,
                        authHeader = "Bearer ${authToken.value}", listOf = "top_rated"
                    )
                if (topRatedrMoviesResponse.isSuccessful && popularMoviesResponse.isSuccessful) {
                    _topRatedMovies.value = topRatedrMoviesResponse.body()
                    _popularMovies.value = popularMoviesResponse.body()
                    _isLoading.value = false
                } else {
                    if (_tries.value <= 1) {
                        _tries.value += 1

                        checkApiAuth()
                        setApiKeyAndFetchData()


                    } else {
                        _tries.value = 0
                        _error.value =
                            "Failed to fetch movie. Please try again. tries: ${_tries.value}"
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
}