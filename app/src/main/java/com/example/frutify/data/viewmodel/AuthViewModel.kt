package com.example.frutify.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.frutify.data.model.LoginResponse
import com.example.frutify.data.model.RegisterResponse
import com.example.frutify.data.network.ApiConfig
import com.example.frutify.utils.Constant.Companion.name
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> = _loginResult

    private val _registerResult = MutableLiveData<RegisterResponse?>()
    val registerResult: LiveData<RegisterResponse?> = _registerResult

    private val _updateResult = MutableLiveData<RegisterResponse?>()
    val updateResult: LiveData<RegisterResponse?> = _updateResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val error = MutableLiveData<String?>()


    fun login(email: String, password: String) {

        _isLoading.value = true
        error.postValue(null)

        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                val loginResponse = response.body()
                if (loginResponse?.STATUS == "SUCCESS") {
                    _loginResult.postValue(loginResponse)
                } else {
                    error.postValue("Login failed. Please check your email and/or password.")
                }

                _isLoading.value = false

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }
        })
    }

    fun register(email: String, password: String, phone: String, role: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(email, password, phone, role)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse?.STATUS == "SUCCESS") {
                        _registerResult.postValue(registerResponse)
                        error.postValue(null) // Clear the error value
                    } else {
                        val errorMessage = registerResponse?.MESSAGE ?: "Unknown error occurred."
                        error.postValue(errorMessage)
                    }
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }

        })
    }

    fun updateUser(
        email: String,
        password: String,
        phone: String,
        fullname: String,
        address: String,
        newPassword: String? = null
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService()
            .updateUser(email, password, phone, fullname, address, newPassword)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val updateResponse = response.body()
                    _updateResult.postValue(updateResponse)
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }

        })
    }


    companion object {
        private val TAG = AuthViewModel::class.simpleName
    }

}