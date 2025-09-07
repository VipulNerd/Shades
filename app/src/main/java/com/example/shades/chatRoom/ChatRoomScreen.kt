package com.example.shades.chatRoom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shades.authentication.AuthViewModel
import com.example.shades.data.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    navController: NavController,
    otherId: String,
    otherName: String,
    authViewModel: AuthViewModel = viewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val myId = currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()
    var chatId by remember { mutableStateOf("") }
    var messages by remember(otherId) { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var text by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var registration by remember { mutableStateOf<ListenerRegistration?>(null) }

    LaunchedEffect(otherId) {
        registration?.remove()
        messages = emptyList()
        chatId = ChatRepository.ensureChat(myId, otherId)
        registration = db.collection("chats").document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snap, _ ->
                if (snap != null) {
                    messages = snap.documents.map { it.data ?: emptyMap() }
                }
            }
    }

    DisposableEffect(Unit) {
        onDispose {
            registration?.remove()
        }
    }
   Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat with $otherName") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(8.dp)
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(messages) { msg ->
                    val senderId = msg["senderId"] as? String ?: ""
                    val messageText = msg["message"] as? String ?: ""
                    Text(
                        text = if (senderId == myId) "You: $messageText" else "$otherName: $messageText",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    val toSend = text.trim()
                    if (toSend.isNotBlank()) {
                        coroutineScope.launch {
                            ChatRepository.sendMessage(chatId, myId, otherId, toSend)
                            text = ""
                        }
                    }
                }) {
                    Text("Send")
                }
            }
        }
    }
}