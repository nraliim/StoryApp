package com.nraliim.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nraliim.storyapp.model.User
import com.nraliim.storyapp.networking.ApiConfig
import com.nraliim.storyapp.response.ApiResponse
import com.nraliim.storyapp.utils.Utils
import okhttp3.MultipartBody
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadImage(
        user: User,
        description: String,
        imageMultipart: MultipartBody.Part,
        callback: Utils.ApiCallback
    ) {
        _isLoading.value = true
        val service = ApiConfig.getApiService()
            .addStories("Bearer ${user.token}", description, imageMultipart)
        service.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback.onResponse(response.body() != null, SUCCESS)
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

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }
        })

    }

    companion object {
        private const val TAG = "AddStoryViewModel"
        private const val SUCCESS = "success"
    }
}