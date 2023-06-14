package com.example.frutify.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.frutify.data.model.ImageClasifyResponse
import com.example.frutify.data.model.RegisterResponse
import com.example.frutify.data.network.ApiConfig
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ClasifyViewModel : ViewModel() {

    private val _imageClasifyResult = MutableLiveData<ImageClasifyResponse?>()
    val imageClasifyResult: LiveData<ImageClasifyResponse?> = _imageClasifyResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val error = MutableLiveData<String?>()

    fun clasifyImage(image: File){
        _isLoading.value = true

        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), image)
        val imagePart = MultipartBody.Part.createFormData("image", image.name, requestFile)

        val client = ApiConfig.getApiService().doImageClasify(imagePart)
        client.enqueue(object : Callback<ImageClasifyResponse>{
            override fun onResponse(
                call: Call<ImageClasifyResponse>,
                response: Response<ImageClasifyResponse>
            ) {
                val imageClasifyResponse = response.body()
                _imageClasifyResult.postValue(imageClasifyResponse)
                _isLoading.value = false

            }

            override fun onFailure(call: Call<ImageClasifyResponse>, t: Throwable) {
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