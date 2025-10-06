package com.example.tassty.model

data class Cart(
    val id: String,
    val name: String,
    val imageUrl: String,
    val price: Int,
    var quantity: Int,
    val stock : Int = 20,
    val note: String? = null,
    val isSwipeActionVisible: Boolean = false,
    val isChecked: Boolean = false
)
