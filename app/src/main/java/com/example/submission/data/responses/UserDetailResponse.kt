package com.example.submission.data.responses

import com.google.gson.annotations.SerializedName

data class UserDetailResponse(
    val bio: String? = null,

    @field:SerializedName("public_repos")
    val publicRepos: Int? = null,
    val followers: Int? = null,

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,
    val following: Int? = null,
    val name: String? = null,
    val location: String? = null,
)
