package com.zavedahmad.cineDrift.ui.searchScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val preferencesDao: PreferencesDao,
    val movieApi: MovieDetailApi
): ViewModel(){
    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    private val _movies = MutableStateFlow<MoviesResponse?>(null)
    val movies = _movies.asStateFlow()
    private val  _authToken = MutableStateFlow<String>("")
    val authToken = _authToken.asStateFlow()

    fun changeSearchQuery(newQuery :String){
        _searchQuery.value= newQuery
    }
    fun setApiKeyAndFetchData(){
        viewModelScope.launch(){
            _authToken.value = preferencesDao.getPreference("ApiKey").value?:  "empty"
            if (_authToken.value != "empty") {
                fetchData()
            } else {
                _error.value = "API key is empty"
            }
        }

    }
    fun fetchData(){
        viewModelScope.launch (Dispatchers.IO){
            try{
                _isLoading.value = true
                _error.value = null // Clear previous errors
                val response = movieApi.getMoviesBySearch(
                    query = _searchQuery.value,
                    includeAdult = false,
                    page = 1,
                    language = "en-US",
                    authHeader = "Bearer ${authToken.value}"
                )
                if(response.isSuccessful){
                    _movies.value =response.body()

                }
            } catch (e: Exception) {
                _error.value = "Failed to fetch movie. Please try again. ${e.message}"
                println("Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}