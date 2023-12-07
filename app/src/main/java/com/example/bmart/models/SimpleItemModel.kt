package com.example.bmart.models
data class SimpleItemModel @JvmOverloads constructor(
    val itemsName: String = "",
    val itemsImage: Int = 0,
    val itemsPrice: Double = 25.00,
    val itemsQuantity: Int = 1
){}