package com.example.shades

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shades.cards.PostCard

@Composable
fun HomeScreen() {
    val posts = listOf(
        Triple("Vipul", null, "Hello this is my first post"),
        Triple("John", null, "Enjoying the sunny day!"),
        Triple("Amit", null, null) // image-only or text-only
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(posts) { (username, image, caption) ->
                PostCard(
                    username = username,
                    image = image,
                    caption = caption
                )
            }
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp)
        ) {
            Text("Post")
        }
    }
}