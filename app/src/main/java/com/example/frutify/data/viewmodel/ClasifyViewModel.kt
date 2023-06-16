package com.example.frutify.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.frutify.data.model.PredictResponse
import com.example.frutify.data.network.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class ClasifyViewModel : ViewModel() {

    private val _imagePredictResult = MutableLiveData<PredictResponse?>()
    val imagePredictResult: LiveData<PredictResponse?> = _imagePredictResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val error = MutableLiveData<String?>()

    fun predictImage(baseUrl: String, image: File){
        _isLoading.value = true

        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), image)
        val imagePart = MultipartBody.Part.createFormData("image", image.name, requestFile)

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val clients = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clients)
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        val client = apiService.doImagePredict(imagePart)
        client.enqueue(object : Callback<PredictResponse>{
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                val imagePredictResponse = response.body()
                _imagePredictResult.postValue(imagePredictResponse)
                _isLoading.value = false

            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }

        })
    }

    companion object {
        private const val TAG = "ClasifyViewModel"
    }
}