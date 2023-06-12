package com.example.frutify.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.frutify.data.model.ListProductResponse
import com.example.frutify.data.model.LoginResponse
import com.example.frutify.data.model.ProductItem
import com.example.frutify.data.model.RegisterResponse
import com.example.frutify.data.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel : ViewModel() {

    private val _productResult = MutableLiveData<List<ProductItem>?>()
    val productResult: LiveData<List<ProductItem>?> = _productResult

    private val _addProductResult = MutableLiveData<RegisterResponse?>()
    val addProductResult: LiveData<RegisterResponse?> = _addProductResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val error = MutableLiveData<String?>()

    fun getListProduct(search: String? = null, userId: Int) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getListProduct(search, userId)
        client.enqueue(object : Callback<ListProductResponse> {
            override fun onResponse(
                call: Call<ListProductResponse>,
                response: Response<ListProductResponse>
            ) {
                if (response.isSuccessful) {
                    val listProductResponse = response.body()
                    val products = listProductResponse?.PAYLOAD?.product
                    _productResult.postValue(products as List<ProductItem>?)
                    error.postValue(null)
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<ListProductResponse>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }
        })
    }

    fun addProduct(
        fruit_id: Int,
        user_id: Int,
        name: String,
        description: String,
        price: Int,
        unit: String,
        filename: String,
        quality: String
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService()
            .addProduct(fruit_id, user_id, name, description, price, unit, filename, quality)
        client.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val addProductResponse = response.body()
                    _addProductResult.postValue(addProductResponse)
                    error.postValue(null)
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }

        })
    }

    companion object {
        private const val TAG = "ProductViewModel"
    }
}
