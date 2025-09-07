package com.example.shades.MyAppNavigation

import android.net.Uri

sealed class ScreenName (val route: String){
    object SignupPage : ScreenName("signIn")
    object LoginPage : ScreenName(route = "login")
    object HomeScreen : ScreenName(route = "home")

    object PostScreen : ScreenName(route = "post")

    object ChatListScreen : ScreenName("chatList")

    object ChatRoomScreen : ScreenName("chatroom") {
        fun createRouteWithUser(otherId: String, otherName: String): String {
            val safeName = Uri.encode(otherName)
            return "$route/$otherId/$safeName"
        }
    }
}