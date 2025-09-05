package com.example.shades


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shades.MyAppNavigation.ScreenName
import com.example.shades.authentication.AuthViewModel
import com.example.shades.cards.PostCard
import com.example.shades.data.PostRepository

@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val posts by PostRepository.posts.collectAsState()

    LaunchedEffect(Unit) {
        PostRepository.fetchPosts()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
    ) {
        LazyColumn {
            items(posts) { post ->
                PostCard(
                    username = post.username,
                    imageUrl = post.mediaUris.firstOrNull(), // safer than forcing cast to Painter
                    caption = post.caption
                )
            }
        }

        Button(
            onClick = { navController.navigate(ScreenName.PostScreen.route) },
            modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp)
        ) {
            Text("Post")
        }
    }
}