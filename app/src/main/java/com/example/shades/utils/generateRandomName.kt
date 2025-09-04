package com.example.shades.utils

fun generateRandomName() : String{
    val adjective = listOf("Brave", "Heart", "Chad", "Swift", "Faded")
    val animal = listOf("Bird", "Lion", "Leopard", "Panda", "Fox")

    val number = (100..999).random()
    return "${adjective.random()}${animal.random()}$number"
}