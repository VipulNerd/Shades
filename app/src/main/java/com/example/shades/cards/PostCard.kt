package com.example.shades.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun PostCard(
    username: String,
    imageUrl: String? = null,
    caption: String? = null
) {
    Column {
        // Username
        Text(
            text = username,
            fontWeight = FontWeight.Bold
        )

        // Image from Firestore (if available)
        if (!imageUrl.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = imageUrl,
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Caption
        if (!caption.isNullOrBlank()) {
            Text(text = caption)
        }
    }
}