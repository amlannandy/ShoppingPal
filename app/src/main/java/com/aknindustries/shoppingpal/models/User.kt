package com.aknindustries.shoppingpal.models

class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val imageUrl: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val profileCompleted: Boolean = false,
)