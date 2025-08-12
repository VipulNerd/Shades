package com.example.shades.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PostCard(
    username: String,
    image: Painter? = null,
    caption: String? = null
) {
    Column {
        Text(
            text = username,
            fontWeight = FontWeight.Bold
        )
        if (image != null) {
            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
            Image(
                painter = image,
                contentDescription = null
            )
            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
        }
        if (!caption.isNullOrBlank()) {
            Text(
                text = caption
            )
        }
    }
}