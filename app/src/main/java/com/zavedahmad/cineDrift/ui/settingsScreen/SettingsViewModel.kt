package com.zavedahmad.cineDrift.ui.settingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.cineDrift.roomDatabase.PreferenceEntity
import com.zavedahmad.cineDrift.roomDatabase.PreferencesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class SettingsViewModel @Inject constructor(val preferencesDao: PreferencesDao) : ViewModel() {
    private val _userInput = MutableStateFlow("")
    val userInput = _userInput.asStateFlow()
    private var api = MutableStateFlow<PreferenceEntity?>(null)

    val apiKeyInDB = api.asStateFlow()

    private val _themeMode = MutableStateFlow<PreferenceEntity?>(null)
    val themeMode= _themeMode.asStateFlow()
        init {
        getApiFromDB()
        collectThemeMode()
    }
    fun setTheme(value: String){
        viewModelScope.launch {
            preferencesDao.updatePreference(PreferenceEntity("ThemeMode", value))
        }

    }
    fun collectThemeMode(){
        viewModelScope.launch (Dispatchers.IO){
            preferencesDao.getPreferenceFlow("ThemeMode").collect { preference-> _themeMode.value = preference }
        }
    }
    fun setUserInput(newInput: String) {
        _userInput.value = newInput
    }

    fun setApiToDB() {
        viewModelScope.launch {
            preferencesDao.updatePreference(PreferenceEntity("ApiKey", _userInput.value))
        }
    }

    fun getApiFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesDao.getPreferenceFlow("ApiKey").collect { preference-> api.value = preference }
        }
    }
}