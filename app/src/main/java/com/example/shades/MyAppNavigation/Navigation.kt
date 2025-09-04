package com.example.shades.MyAppNavigation

import AuthViewModel
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shades.HomeScreen
import com.example.shades.authentication.LogIn
import com.example.shades.authentication.SignUp
import com.example.shades.cards.PostScreen
import com.example.shades.cards.PostViewModel


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ScreenName.LoginPage.route) {
        composable(ScreenName.LoginPage.route) {
            val loginViewModel: AuthViewModel = viewModel()
            LogIn(
                navController = navController,
                viewModel = loginViewModel
            )
        }

        composable(ScreenName.SignupPage.route) {
            val signUpViewModel: AuthViewModel = viewModel()
            SignUp(
                navController = navController,
                viewModel = signUpViewModel
            )
        }

        composable(ScreenName.HomeScreen.route){
            HomeScreen(
                navController = navController
            )
        }

        composable(ScreenName.PostScreen.route) {
            val postViewModel: PostViewModel = viewModel()
            PostScreen(
                navController = navController,
                viewModel = postViewModel
            )
        }
    }
}