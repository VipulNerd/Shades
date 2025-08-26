package com.example.shades.MyAppNavigation

sealed class ScreenName (val route: String){
    object SignupPage : ScreenName("signIn")
    object LoginPage : ScreenName(route = "login")
    object HomeScreen : ScreenName(route = "home")

    object PostScreen : ScreenName(route = "post")
}