package com.example.shades.cards

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun PostScreen(
    onPost: (String, List<Uri>) -> Unit={_,_->}
){
    var description by remember { mutableStateOf("") }
    var mediaUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val wordCount = description.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
    Column {
        OutlinedTextField(
            value = description,
            onValueChange = {newText->
                val newWordCnt = newText.trim().split("\\+s".toRegex()).filter { it.isNotEmpty() }.size
                if(newWordCnt <= 200){
                    description = newText
                }
            },
            label = { Text("What's on your mind? (max 200 words)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 6
        )
        Spacer(modifier = Modifier.height(8.dp))
        if(mediaUris.isNotEmpty()){
            LazyColumn {
                items(mediaUris.size){index ->
                    val uri = mediaUris[index]
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected media",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier,
            Arrangement.SpaceBetween
        ){
            Button(onClick = {}){
                Text("image")
            }
            Button(onClick = {}){
                Text("video")
            }
            Button(onClick = {}){
                Text("audio")
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            {onPost(description, mediaUris)},
            enabled = description.isNotEmpty()||mediaUris.isNotEmpty()
        ){
            Text("Post")
        }
    }
}

