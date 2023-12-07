package com.example.bmart.models

data class VendorModel @JvmOverloads constructor(
    val vendorsName: String = "",
    val vendorsImage: String = "",
    val vendorsRating: Double = 0.0,
    val vendorsLocation: String = "",
    var documentId: String = "",
    var isFavorite: Boolean = false,
)