package com.up.productmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

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
