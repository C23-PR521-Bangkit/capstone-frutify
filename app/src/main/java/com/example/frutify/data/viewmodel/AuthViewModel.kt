package com.example.frutify.data.viewmodel

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.frutify.data.model.Login
import com.example.frutify.data.model.LoginResponse
import com.example.frutify.data.model.Register
import com.example.frutify.data.network.ApiConfig
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

//    private val _registerResult = MutableLiveData<Register>()
//    val registerResult: LiveData<Register> = _registerResult

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    val error = MutableLiveData<String>()


    fun login(email: String, password: String) {
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _loginResult.postValue(response.body())
                }
//                }else {
//                    response.errorBody()?.let {
//                        val errorResponse = JSONObject(it.string())
//                        val errorMessages = errorResponse.getString("message")
//                        error.postValue("LOGIN ERROR : $errorMessages")
//                    }
//                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }

        })
    }

//    fun register(name: String, email: String, password: String) {
//        _isLoading.value = true
//        val client = ApiConfig.getApiService().register(name, email, password)
//        client.enqueue(object : Callback<Register> {
//            override fun onResponse(
//                call: Call<Register>,
//                response: Response<Register>
//            ) {
//                when (response.code()) {
//                    201 -> _registerSuccess.postValue(true)
//                    400 -> error.postValue("invalid email format")
//                    401 -> error.postValue("can't find email/not registered")
//                    else -> {
//                        error.postValue("Error ${response.code()} : ${response.message()}")
//                    }
//                }
//                _isLoading.value = false
//            }
//
//            override fun onFailure(call: Call<Register>, t: Throwable) {
//                _isLoading.value = false
//                Log.e(TAG, "onFailure Call: ${t.message}")
//                error.postValue(t.message)
//            }
//
//        })
//    }


    companion object {
        private val TAG = AuthViewModel::class.simpleName
    }

}