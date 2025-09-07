package com.example.shades


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
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
                    Surface(
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 4.dp,
                        shadowElevation = 4.dp,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        PostCard(
                            username = post.username,
                            imageUrl = post.mediaUris.firstOrNull(),
                            caption = post.caption,
                            onUsernameClick = {
                                navController.navigate(
                                    ScreenName.ChatRoomScreen.createRouteWithUser(
                                        post.authorId,
                                        post.username
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}