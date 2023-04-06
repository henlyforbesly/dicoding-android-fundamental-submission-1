package com.example.submission.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission.data.network.ApiConfig
import com.example.submission.data.responses.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun getUserFollowers(username: String) {
        _isLoading.value = true

        ApiConfig.getApiService().getUserFollowers(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _isError.value = false
                    response.body()?.let {
                        _users.value = it
                    }
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }

    fun getUserFollowing(username: String) {
        _isLoading.value = true

        ApiConfig.getApiService().getUserFollowing(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _isError.value = false
                    response.body()?.let {
                        _users.value = it
                    }
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}