package com.example.frutify.ui.dashboard.cart

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.frutify.R
import com.example.frutify.data.model.ListProductResponse
import com.example.frutify.data.model.ProductItem
import com.example.frutify.data.network.ApiConfig
import com.example.frutify.data.viewmodel.ProductViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity {

    private val _productResult = MutableLiveData<List<ProductItem>?>()
    val productResult: LiveData<List<ProductItem>?> = _productResult

    val error = MutableLiveData<String?>()

    fun getListProduct(search: String? = null, userId: Int) {

        val client = ApiConfig.getApiService().getListProduct(search, userId)
        client.enqueue(object : Callback<ListProductResponse> {
            override fun onResponse(
                call: Call<ListProductResponse>,
                response: Response<ListProductResponse>
            ) {
                if (response.isSuccessful) {
                    val listProductResponse = response.body()
                    val products = listProductResponse?.PAYLOAD?.product
                    //_productResult.postValue(products as List<ProductItem>?)
                    error.postValue(null)
                }
            }

            override fun onFailure(call: Call<ListProductResponse>, t: Throwable) {
                Log.e("HBB", "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }
        })
    }
}