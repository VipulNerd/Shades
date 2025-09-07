package com.example.shades.cards


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shades.MyAppNavigation.BottomNavigationBar
import com.example.shades.MyAppNavigation.ScreenName
import com.example.shades.authentication.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun PostScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    viewModel: PostViewModel = viewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val username = currentUser?.displayName ?: "Anonymous"
    val isButtonEnabled = username.isNotBlank() && (viewModel.description.isNotEmpty() || viewModel.mediaUris.isNotEmpty())


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.addMediaUris(it) }
    }
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.addMediaUris(it) }
    }
    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.addMediaUris(it) }
    }
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope= rememberCoroutineScope()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, currentRoute = ScreenName.PostScreen.route) },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { newText -> viewModel.onDescriptionChange(newText) },
                label = { Text("What's on your mind? (max 200 words)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 6,
                textStyle = MaterialTheme.typography.bodyLarge
            )

            if (viewModel.mediaUris.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.mediaUris.size) { index ->
                        val uri = viewModel.mediaUris[index]
                        when {
                            uri.toString().contains("image") -> {
                                Card(
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(uri),
                                        contentDescription = "Selected image",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                    )
                                }
                            }
                            uri.toString().contains("video") -> {
                                Text("🎥 Video selected: $uri", color = MaterialTheme.colorScheme.primary)
                            }
                            else -> {
                                Text("🎙️ Audio selected: $uri", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text("📷 Image")
                }
                Button(onClick = { videoPickerLauncher.launch("video/*") }) {
                    Text("🎥 Video")
                }
                Button(onClick = { audioPickerLauncher.launch("audio/*") }) {
                    Text("🎙️ Audio")
                }
            }

            Button(
                onClick = {
                    if (currentUser == null || username.isBlank()) {
                        coroutineScope.launch { snackBarHostState.showSnackbar("Loading user, please wait") }
                        return@Button
                    }
                    coroutineScope.launch {
                        val success = viewModel.submitPost(
                            authorId = currentUser!!.uid,
                            username = username
                        )
                        if (success) {
                            snackBarHostState.showSnackbar("Post uploaded successfully!")
                            navController.popBackStack()
                        } else {
                            snackBarHostState.showSnackbar("Failed to upload post!")
                        }
                    }
                },
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🚀 Post")
            }
        }
    }
}
