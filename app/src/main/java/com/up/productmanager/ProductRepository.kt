package com.up.productmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun getAllProducts() {
        RetrofitClient.api.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    _products.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                _message.postValue("Error al obtener productos: ${t.message}")
            }
        })
    }

    fun createNewProduct(product: ProductCreate) {
        RetrofitClient.api.createProduct(product).enqueue(object  : Callback<Product> {
            override fun onResponse(call: Call<Product?>, response: Response<Product>) {
                if (response.isSuccessful) {
                    _message.postValue("Producto creado correctamente")
                    getAllProducts()
                } else {
                    _message.postValue("Error al crear producto: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Product?>, t: Throwable) {
                _message.postValue("Error de conexión: ${t.message}")
            }
        })
    }

    fun deleteProductByID(id: Int) {
        RetrofitClient.api.deleteProduct(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    _message.postValue("Producto eliminado correctamente")
                    getAllProducts()
                } else {
                    _message.postValue("Error al eliminar producto: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _message.postValue("Error de conexión: ${t.message}")
            }
        })
    }
}
