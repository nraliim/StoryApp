package com.nraliim.storyapp.response

import com.google.gson.annotations.SerializedName

data class ApiResponse(

    @field:SerializedName("error")
    val error: String,

    @field:SerializedName("message")
    val message: String
)
