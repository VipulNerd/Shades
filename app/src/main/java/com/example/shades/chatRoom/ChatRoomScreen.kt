package com.example.shades.chatRoom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shades.authentication.AuthViewModel
import com.example.shades.data.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

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
    val listState = rememberLazyListState()

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

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    DisposableEffect(Unit) {
        onDispose { registration?.remove() }
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(messages) { msg ->
                    val senderId = msg["senderId"] as? String ?: ""
                    val messageText = msg["message"] as? String ?: ""

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = if (senderId == myId) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (senderId == myId)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (senderId == myId)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSurface,
                            shadowElevation = 2.dp,
                            modifier = if (senderId == myId)
                                Modifier.padding(start = 48.dp, end = 4.dp)
                            else
                                Modifier.padding(start = 4.dp, end = 48.dp)
                        ) {
                            Text(
                                text = messageText,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Type a message…") },
                    modifier = Modifier.weight(1f),
                    maxLines = 4
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