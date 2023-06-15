package com.example.frutify.data.viewmodel

import android.telecom.Call
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.frutify.data.model.ProductItem
import com.example.frutify.data.model.RegisterResponse
import com.example.frutify.data.network.ApiConfig
import retrofit2.Callback
import retrofit2.Response

class CartViewModel : ViewModel() {

    private val _addCartResult = MutableLiveData<RegisterResponse?>()
    val addCartResult: LiveData<RegisterResponse?> = _addCartResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val error = MutableLiveData<String?>()

    fun addToCart(userId: Int, productId: Int, qty: Int){
        _isLoading.value = true

        val client = ApiConfig.getApiService().addToCart(userId, productId, qty)
        client.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: retrofit2.Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val addCartResponse = response.body()
                    _addCartResult.postValue(addCartResponse)
                    error.postValue(null)
                }
                _isLoading.value = false
            }

            override fun onFailure(call: retrofit2.Call<RegisterResponse>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }

        })
    }

    companion object {
        private const val TAG = "CartViewModel"
    }
}