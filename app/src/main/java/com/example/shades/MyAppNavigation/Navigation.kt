package com.example.shades.MyAppNavigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shades.HomeScreen
import com.example.shades.authentication.LogIn
import com.example.shades.authentication.SignUp

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ScreenName.LoginPage.route) {
        composable(ScreenName.LoginPage.route) {
            LogIn(
                navController = navController
            )
        }

        composable(ScreenName.SignupPage.route) {
            SignUp(
                navController = navController
            )
        }

        composable(ScreenName.HomeScreen.route){
            HomeScreen(
            )
        }
    }

}