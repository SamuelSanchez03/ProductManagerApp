package com.up.productmanager

data class Product(
    val id : Int,
    val nombre : String,
    val precio : Double,
    val cantidad_disponible: Int
)

data class ProductCreate(
    val nombre: String,
    val precio: Double,
    val cantidad_disponible: Int
)
