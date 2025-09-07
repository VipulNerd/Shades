package com.example.shades


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.shades.MyAppNavigation.BottomNavigationBar
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

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, currentRoute = ScreenName.HomeScreen.route) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn {
                items(posts) { post ->
                    PostCard(
                        username = post.username,
                        imageUrl = post.mediaUris.firstOrNull(),
                        caption = post.caption,
                        oneUsernameClick = {
                            navController.navigate(
                                ScreenName.ChatRoomScreen.createRouteWithUser(post.authorId, post.username)
                            )
                        }
                    )
                }
            }
        }
    }
}