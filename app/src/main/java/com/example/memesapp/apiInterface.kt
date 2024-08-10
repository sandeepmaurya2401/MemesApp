package com.example.memesapp


import retrofit2.http.GET

interface apiInterface {

    @GET("gimme")
    fun getProductData(): retrofit2.Call<Mydata>
}