package com.up.productmanager

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val BASE_URL = "http://10.0.2.2:8000/"
    private val TAG = "CHECK_RESPONSE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        getAllProducts()
    }

    private fun getAllProducts() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)

        api.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>?>,
                response: Response<List<Product>?>
            ) {
                if(response.isSuccessful) {
                    response.body()?.let {
                        for(comment in it) {
                            Log.i(TAG, "onResponse: ${comment.nombre}")
                        }
                    }
                }
            }

            override fun onFailure(
                call: Call<List<Product>?>,
                t: Throwable
            ) {
                Log.i(TAG, "onFailure: ${t.message}")
            }

        })
    }
}