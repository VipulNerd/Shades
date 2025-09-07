package com.example.shades.chatRoom

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shades.MyAppNavigation.BottomNavigationBar
import com.example.shades.MyAppNavigation.ScreenName
import com.example.shades.authentication.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ChatListScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val uid = currentUser?.uid
    val coroutineScope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()
    var chats by remember { mutableStateOf<List<com.google.firebase.firestore.DocumentSnapshot>>(emptyList()) }

    // Listener lifecycle is tied to uid
    DisposableEffect(uid) {
        if (uid == null) {
            onDispose { /* no-op */ }
        } else {
            val query = db.collection("chats").whereArrayContains("participants", uid)
            val reg = query.addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    chats = snapshot.documents
                }
            }
            onDispose { reg.remove() }
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController,currentRoute = ScreenName.ChatListScreen.route) }
    ) {innerPadding->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // Keep composition stable: show a placeholder when uid is null
            if (uid == null) {
                Text("Loading chats...", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(chats) { chatDoc ->
                        val participants = chatDoc.get("participants") as? List<*>
                        val otherId = participants?.firstOrNull { it != uid } as? String
                        val lastMessage = chatDoc.getString("lastMessage") ?: ""
                        var otherName by remember(otherId) { mutableStateOf(otherId ?: "Unknown") }

                        // Fetch display name only when otherId changes and exists
                        LaunchedEffect(otherId) {
                            if (!otherId.isNullOrEmpty()) {
                                val userSnap = FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(otherId)
                                    .get()
                                    .await()
                                otherName = userSnap.getString("displayName") ?: otherId
                            }
                        }

                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier
                                .clickable {
                                    Log.d(
                                        "ChatList",
                                        "Click: participants=$participants, otherId=$otherId, otherName=$otherName"
                                    )
                                    if (!otherId.isNullOrEmpty()) {
                                        try {
                                            navController.navigate(
                                                ScreenName.ChatRoomScreen.createRouteWithUser(
                                                    otherId,
                                                    otherName
                                                )
                                            )
                                        } catch (e: Exception) {
                                            // prevent crash — optionally log to Logcat
                                            Log.e("ChatList", "navigate to chatroom failed", e)
                                        }
                                    }
                                }
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = otherName,
                                    style = androidx.compose.ui.text.TextStyle.Default.copy(
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = lastMessage,
                                    style = androidx.compose.ui.text.TextStyle.Default.copy(
                                        fontSize = androidx.compose.ui.unit.TextUnit.Unspecified,
                                        color = androidx.compose.ui.graphics.Color.Gray
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}