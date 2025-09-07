package com.example.shades.cards

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.util.*

class PostViewModel : ViewModel() {
    var description by mutableStateOf("")
        private set
    var mediaUris by mutableStateOf<List<Uri>>(emptyList())
        private set
    val wordCount: Int
        get() = description.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size

    private val db = FirebaseFirestore.getInstance()

    fun onDescriptionChange(newDescription: String) {
        val newWordCount = newDescription.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
        if (newWordCount <= 200) {
            description = newDescription
        }
    }

    fun addMediaUris(newMediaUri: Uri) {
        mediaUris = mediaUris + newMediaUri
    }
    suspend fun submitPost(authorId: String, username: String): Boolean {
        return try {
            val postId = UUID.randomUUID().toString()
            val postData = hashMapOf(
                "postId" to postId,
                "authorId" to authorId,
                "username" to username,
                "caption" to description,
                "mediaUris" to mediaUris.map { it.toString() },
                "timestamp" to System.currentTimeMillis()
            )

            db.collection("posts").document(postId)
                .set(postData, SetOptions.merge())
                .await()

            description = ""
            mediaUris = emptyList()
            true
        } catch (e: Exception) {
            false
        }
    }

    data class Post(
        val postId: String = "",
        val authorId: String = "",
        val username: String = "",
        val mediaUris: List<String> = emptyList(),
        val caption: String? = null,
        val timestamp: Long = System.currentTimeMillis()
    )
}