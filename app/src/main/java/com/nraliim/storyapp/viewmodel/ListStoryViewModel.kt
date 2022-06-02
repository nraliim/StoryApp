package com.nraliim.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nraliim.storyapp.networking.ApiConfig
import com.nraliim.storyapp.response.StoriesResponse
import com.nraliim.storyapp.response.StoryItem
import com.nraliim.storyapp.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListStoryViewModel : ViewModel() {
    private val _itemStory = MutableLiveData<List<StoryItem>>()
    val itemStory: LiveData<List<StoryItem>> = _itemStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isAvailable = MutableLiveData<Boolean>()
    val isAvailable: LiveData<Boolean> = _isAvailable

    private val _snackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = _snackBarText

    fun showListStory(token: String) {
        _isLoading.value = true
        _isAvailable.value = true
        val client = ApiConfig
            .getApiService()
            .getAllStories("Bearer $token")

        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (!responseBody.error) {
                            _itemStory.value = response.body()?.listStory
                            _isAvailable.value =
                                responseBody.message == "Stories fetched successfully"
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _snackBarText.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _snackBarText.value = Event(t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "ListStoryViewModel"
    }
}