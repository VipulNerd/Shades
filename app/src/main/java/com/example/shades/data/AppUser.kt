package com.example.shades.data

data class AppUser(
    val uid: String,
    val email: String,
    val displayName: String,
    val createdAt: Long = System.currentTimeMillis()
)
