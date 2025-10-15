package com.up.productmanager.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.up.productmanager.data.model.ProductCreate
import com.up.productmanager.ui.main.ProductViewModel
import com.up.productmanager.R

class MainActivity : AppCompatActivity() {

    private val viewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Observa cambios en la lista
        viewModel.products.observe(this, Observer { lista ->
            lista.forEach {
                Log.d("PRODUCTS", "${it.id}: ${it.nombre} - ${it.precio}")
            }
        })

        // Observa mensajes (éxito / error)
        viewModel.message.observe(this, Observer { msg ->
            Log.i("API_MESSAGE", msg)
        })

        // Cargar productos al iniciar
        viewModel.loadProducts()

        // Ejemplo: Crear un producto nuevo
        val newProduct = ProductCreate("Test", 299.99, 15)
        viewModel.createProduct(newProduct)

        // Ejemplo: Eliminar un producto (ID 2)
        // viewModel.deleteProduct(4)
    }
}