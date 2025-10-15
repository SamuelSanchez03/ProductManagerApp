package com.up.productmanager.data.network
import com.up.productmanager.data.model.Product
import com.up.productmanager.data.model.ProductCreate
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
