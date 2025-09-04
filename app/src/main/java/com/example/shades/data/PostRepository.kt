package com.example.shades.data

import com.example.shades.cards.PostViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PostRepository {
    private val _posts = MutableStateFlow<List<PostViewModel.Post>>(emptyList())
    val posts: StateFlow<List<PostViewModel.Post>> = _posts

    fun addPost(post: PostViewModel.Post){
        _posts.value = _posts.value + post
    }
}