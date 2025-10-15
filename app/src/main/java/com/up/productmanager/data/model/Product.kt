package com.up.productmanager.data.model

data class Product(
    val id : Int,
    val nombre : String,
    val precio : Double,
    val cantidad_disponible: Int
)