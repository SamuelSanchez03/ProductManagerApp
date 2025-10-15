package com.up.productmanager.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.up.productmanager.data.model.Product
import com.up.productmanager.data.model.ProductCreate
import com.up.productmanager.data.repository.ProductRepository

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()
    val products: LiveData<List<Product>> = repository.products
    val message: LiveData<String> = repository.message

    fun loadProducts() {
        repository.getAllProducts()
    }

    fun createProduct(product: ProductCreate) {
        repository.createNewProduct(product)
    }

    fun deleteProduct(id: Int) {
        repository.deleteProductByID(id)
    }
}