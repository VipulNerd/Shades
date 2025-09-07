package com.example.shades.MyAppNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.shades.authentication.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BottomNavigationBar(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    currentRoute: String? = null
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    // If no user, don't show the bottom bar
    if (currentUser == null) return

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == ScreenName.HomeScreen.route,
            onClick = {
                navController.navigate(ScreenName.HomeScreen.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Chat, contentDescription = "Chat") },
            label = { Text("Chat") },
            selected = currentRoute == ScreenName.ChatListScreen.route,
            onClick = {
                navController.navigate(ScreenName.ChatListScreen.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Logout, contentDescription = "Logout") },
            label = { Text("Logout") },
            selected = currentRoute == ScreenName.LoginPage.route,
            onClick = {
                authViewModel.logout()
                navController.navigate(ScreenName.LoginPage.route){
                    popUpTo(ScreenName.HomeScreen.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.PostAdd, contentDescription = "Post") },
            label = { Text("Post") },
            selected = currentRoute == ScreenName.PostScreen.route,
            onClick = {
                navController.navigate(ScreenName.PostScreen.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}