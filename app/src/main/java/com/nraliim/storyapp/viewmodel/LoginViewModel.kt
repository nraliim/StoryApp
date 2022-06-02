package com.nraliim.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nraliim.storyapp.model.User
import com.nraliim.storyapp.networking.ApiConfig
import com.nraliim.storyapp.preferences.UserPreferences
import com.nraliim.storyapp.response.LoginResponse
import com.nraliim.storyapp.utils.Utils
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String, callback: Utils.ApiCallback) {
        _isLoading.value = true

        val service = ApiConfig.getApiService().login(email, password)
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {

                        callback.onResponse(response.body() != null, SUCCESS)

                        val model = User(
                            responseBody.loginResult.name,
                            email,
                            password,
                            responseBody.loginResult.userId,
                            responseBody.loginResult.token,
                            true
                        )
                        saveUser(model)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")

                    // get message error
                    val jsonObject =
                        JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }
        })

    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            userPreferences.saveUser(user)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
        private const val SUCCESS = "success"
    }
}