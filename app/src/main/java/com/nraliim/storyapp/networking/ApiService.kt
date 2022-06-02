package com.nraliim.storyapp.networking

import com.nraliim.storyapp.response.ApiResponse
import com.nraliim.storyapp.response.LoginResponse
import com.nraliim.storyapp.response.StoriesResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<ApiResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<LoginResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token : String
    ) : Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun addStories(
        @Header("Authorization") token: String,
        @Part("description") description: String,
        @Part file: MultipartBody.Part
    ) : Call<ApiResponse>
}