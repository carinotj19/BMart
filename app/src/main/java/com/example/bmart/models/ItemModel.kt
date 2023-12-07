package com.example.bmart.models
data class ItemModel @JvmOverloads constructor(
    val vendorName: String = "",
    val itemsName: String = "",
    val itemsImage: String = "",
    val itemsPrice: Double = 25.00,
    val documentId: String = "",
){}