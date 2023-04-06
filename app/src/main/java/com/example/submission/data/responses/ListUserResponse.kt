package com.example.submission.data.responses

import com.google.gson.annotations.SerializedName

data class ListUserResponse(
    val items: List<User>
)

data class User(
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,
)
