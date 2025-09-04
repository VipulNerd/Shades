package com.example.shades.data

import com.example.shades.cards.PostViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PostRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val postsCollection = firestore.collection("posts")

    private val _posts = MutableStateFlow<List<PostViewModel.Post>>(emptyList())
    val posts: StateFlow<List<PostViewModel.Post>> = _posts

    private var listenerRegistration: ListenerRegistration? = null

    fun addPost(post: PostViewModel.Post) {
        postsCollection.add(post)
    }

    fun fetchPosts() {
        listenerRegistration?.remove()
        listenerRegistration = postsCollection
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error if needed
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val postList = snapshot.toObjects(PostViewModel.Post::class.java)
                    _posts.value = postList
                }
            }
    }
}