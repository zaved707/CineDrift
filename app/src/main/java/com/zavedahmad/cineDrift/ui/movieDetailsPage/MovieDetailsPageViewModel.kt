package com.zavedahmad.cineDrift.ui.movieDetailsPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.cineDrift.AuthorizationModel

import com.zavedahmad.cineDrift.Screen.MovieDetailPageRoute
import com.zavedahmad.cineDrift.movieDetailModel.MovieDetailModel
import com.zavedahmad.cineDrift.retrofitApi.MovieDetailApi
import com.zavedahmad.cineDrift.roomDatabase.FavouritesDao
import com.zavedahmad.cineDrift.roomDatabase.FavouritesEntity
//import com.zavedahmad.cineDrift.roomDatabase.FavouritesDao
//import com.zavedahmad.cineDrift.roomDatabase.FavouritesEntity
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
    val movieDetailApi: MovieDetailApi,
    val preferencesDao: PreferencesDao,
    val favouritesDao: FavouritesDao
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: MovieDetailPageRoute): MovieDetailsPageViewModel
    }


    private val _movie = MutableStateFlow<MovieDetailModel?>(null)
    val movie = _movie.asStateFlow()
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    private val _authToken = MutableStateFlow<String>("")
    val authToken = _authToken.asStateFlow()
    private val _isFavourite = MutableStateFlow<Boolean?>(null)
    val isFavourite = _isFavourite.asStateFlow()
    private val _isAuthenticated = MutableStateFlow<AuthorizationModel?>(null)
    val isAuthenticated = _isAuthenticated.asStateFlow()
    private val _tries = MutableStateFlow(0)

    init {
        println("viewModel Created")
        reloadFromScreen()
        checkFavourite()
    }
    fun reloadFromScreen() {
        _isAuthenticated.value = AuthorizationModel(true)
        _tries.value = 0
        setApiKeyAndFetchData()
    }
    override fun onCleared() {
        println("details Page ViewModel Deleted")
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

    fun checkFavourite() {
        viewModelScope.launch(Dispatchers.IO) {
            _isFavourite.value = favouritesDao.checkMovieInFavourites(navKey.id)

        }

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

    fun addToFavourite() {

        _movie.value?.let { item ->
            viewModelScope.launch(Dispatchers.IO) {
                favouritesDao.addMovie(FavouritesEntity(navKey.id, item))
                checkFavourite()
            }
        }

    }

    fun deleteFromFavourite() {
        viewModelScope.launch(Dispatchers.IO) {
            favouritesDao.deleteFromFavourites(navKey.id)
            checkFavourite()
        }

    }

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                _error.value = null // Clear previous errors
                val response = movieDetailApi.getMovie(
                    movieId = navKey.id,
                    language = "en-US",
                    authHeader = "Bearer ${authToken.value}"
                )
                if (response.isSuccessful) {
                    _movie.value = response.body()
                    _isLoading.value= false
                }  else {
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
}

