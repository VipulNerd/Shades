package com.example.shades.MyAppNavigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shades.HomeScreen
import com.example.shades.authentication.AuthViewModel
import com.example.shades.authentication.LogIn
import com.example.shades.authentication.SignUp
import com.example.shades.cards.PostScreen
import com.example.shades.cards.PostViewModel
import com.example.shades.chatRoom.ChatListScreen
import com.example.shades.chatRoom.ChatRoomScreen
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel() // shared instance
    // react to auth state and route to login/home automatically
    val currentUser = authViewModel.currentUser.collectAsState().value
    LaunchedEffect(currentUser) {
        val currentRoute = navController.currentDestination?.route
        if (currentUser == null) {
            // only navigate if we are not already on Login
            if (currentRoute != ScreenName.LoginPage.route) {
                navController.navigate(ScreenName.LoginPage.route) {
                    // clear all destinations from the backstack to avoid returning to app screens
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        } else {
            // user logged in — open home only if not already there
            if (currentRoute != ScreenName.HomeScreen.route) {
                navController.navigate(ScreenName.HomeScreen.route) {
                    popUpTo(ScreenName.LoginPage.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = ScreenName.LoginPage.route) {

        composable(ScreenName.LoginPage.route) {
            LogIn(navController = navController, viewModel = authViewModel)
        }

        composable(ScreenName.SignupPage.route) {
            SignUp(navController = navController, viewModel = authViewModel)
        }

        composable(ScreenName.HomeScreen.route) {
            HomeScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(ScreenName.PostScreen.route) {
            val postViewModel: PostViewModel = viewModel()
            PostScreen(navController = navController, authViewModel = authViewModel, viewModel = postViewModel)
        }

        composable(ScreenName.ChatListScreen.route) {
            ChatListScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(
            route = ScreenName.ChatRoomScreen.route + "/{otherId}/{otherName}",
            arguments = listOf(
                navArgument("otherId") { type = NavType.StringType },
                navArgument("otherName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val otherId = backStackEntry.arguments?.getString("otherId") ?: ""
            val otherName = backStackEntry.arguments?.getString("otherName") ?: "Unknown"
            ChatRoomScreen(
                navController = navController,
                otherId = otherId,
                otherName = otherName,
                authViewModel = authViewModel
            )
        }
    }
}