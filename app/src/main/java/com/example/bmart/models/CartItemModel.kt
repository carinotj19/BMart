package com.example.bmart.models

data class CartItemModel @JvmOverloads constructor(
    var itemsImage: String = "",
    var itemsName: String = "",
    var vendorsName: String = "",
    var itemPrice: String = "",
    var isSelected: Boolean = false)
{
    var quantity: Int = 1
}