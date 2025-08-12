package com.example.shades

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.shades.cards.PostCard

@Composable
fun HomeScreen(

) {
    val posts = listOf(
        Triple("Vipul", null, "Hello this is my first post"),
        Triple("John", null, "Enjoying the sunny day!"),
        Triple("Amit", null, null) // image-only or text-only
    )

    LazyColumn {
        items(posts.size) { index ->
            val (username, image, caption) = posts[index]
            PostCard(
                username = username,
                image = image,
                caption = caption
            )
        }
    }
}