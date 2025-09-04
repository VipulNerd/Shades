package com.example.shades.cards

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.shades.data.PostRepository
import kotlin.text.trim

class PostViewModel: ViewModel() {
    var description by mutableStateOf("")
        private set
    var mediaUris by mutableStateOf<List<Uri>>(emptyList())
        private set
    val wordCount: Int
        get() = description.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size

    fun onDescriptionChange(newDescription: String) {
        val newWordCount = newDescription.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
        if(newWordCount <= 200) {
            description = newDescription
        }
    }

    fun addMediaUris(newMediaUris: Uri) {
        mediaUris = mediaUris + newMediaUris
    }

    fun submitPost(onPost: (String, List<Uri>) -> Unit) {
        val post = Post("Vipul", mediaUris, description)
        PostRepository.addPost(post)
        onPost(description, mediaUris)
        description = ""
        mediaUris = emptyList()
    }

    data class Post(
        val username: String,
        val mediaUris: List<Uri> = emptyList(),
        val caption: String? = null
    )
}
