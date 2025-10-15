package com.up.productmanager
import retrofit2.Call
import retrofit2.http.*

interface ProductAPI {
    @GET("productos")
    fun getProducts(): Call<List<Product>>

    @POST("productos")
    fun createProduct(@Body producto: ProductCreate): Call<Product>

    @DELETE("productos/{id}")
    fun deleteProduct(@Path("id") id: Int): Call<Void>
}
