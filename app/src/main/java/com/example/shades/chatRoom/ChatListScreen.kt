package com.example.shades.chatRoom

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (!otherId.isNullOrEmpty()) {
                                        navController.navigate(
                                            ScreenName.ChatRoomScreen.createRouteWithUser(
                                                otherId,
                                                otherName
                                            )
                                        )
                                    }
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            shape = MaterialTheme.shapes.medium,
                            tonalElevation = 2.dp
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = otherName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = lastMessage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}