package com.up.productmanager

import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("productos")
    fun getProducts(): Call<List<Product>>
}