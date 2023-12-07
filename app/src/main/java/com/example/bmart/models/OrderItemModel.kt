package com.example.bmart.models

data class OrderHistoryDate(val date: String)

data class OrderItem(
    val itemName: String,
    val itemImage: Int, // Use the appropriate type for your image representation
    val quantity: Int,
    val price: Double
)