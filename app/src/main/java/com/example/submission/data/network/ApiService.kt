package com.example.submission.data.network

import com.example.submission.data.responses.ListUserResponse
import com.example.submission.data.responses.User
import com.example.submission.data.responses.UserDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    fun searchUsers(
        @Query("q") q: String
    ): Call<ListUserResponse>

    @GET("/users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("/users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<User>>

    @GET("/users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<User>>
}
