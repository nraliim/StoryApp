package com.nraliim.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nraliim.storyapp.model.User
import com.nraliim.storyapp.preferences.UserPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    fun getUser(): LiveData<User> {
        return userPreferences.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
        }
    }
}