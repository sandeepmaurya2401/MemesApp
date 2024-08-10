package com.example.memesapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivityViewModel: ViewModel() {

    private var mutableLiveData = MutableLiveData<String>()

    val liveData: LiveData<String>
        get() = mutableLiveData

    fun loadMeme(){
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://meme-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(apiInterface::class.java)

        val getData = retrofitBuilder.getProductData()
        getData.enqueue(object : Callback<Mydata?> {
            override fun onResponse(p0: Call<Mydata?>, p1: Response<Mydata?>) {
                val responseBody = p1.body()
                val data = responseBody?.url
                mutableLiveData.value = data
            }

            override fun onFailure(p0: Call<Mydata?>, p1: Throwable) {
                Log.d("Main Activity", "Error: ${p1.message}")
            }
        })
    }
}