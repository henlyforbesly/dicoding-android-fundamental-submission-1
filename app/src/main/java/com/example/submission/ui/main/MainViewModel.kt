package com.example.submission.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission.data.network.ApiConfig
import com.example.submission.data.responses.ListUserResponse
import com.example.submission.data.responses.User
import com.example.submission.shared.Constants.Companion.INIT_SEARCH_USERS_QUERY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    internal val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    init {
        searchUsers(INIT_SEARCH_USERS_QUERY)
    }

    fun searchUsers(q: String) {
        _isLoading.value = true

        ApiConfig.getApiService().searchUsers(q).enqueue(object : Callback<ListUserResponse> {
            override fun onResponse(
                call: Call<ListUserResponse>,
                response: Response<ListUserResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _isError.value = false
                    response.body()?.let {
                        _users.value = it.items
                    }
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<ListUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}