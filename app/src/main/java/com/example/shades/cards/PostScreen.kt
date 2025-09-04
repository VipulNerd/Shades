package com.example.shades.cards

import AuthViewModel
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
import androidx.compose.material3.Button
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
import com.example.shades.MyAppNavigation.ScreenName
import kotlinx.coroutines.launch

@Composable
fun PostScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    viewModel: PostViewModel = viewModel()
){
    val currentUsername by authViewModel.currentUser.collectAsState()
    val username = currentUsername?.displayName ?: "Anonymous"
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
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { newText ->
                    viewModel.onDescriptionChange(newText)
                },
                label = { Text("What's on your mind? (max 200 words)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 6
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (viewModel.mediaUris.isNotEmpty()) {
                LazyColumn {
                    items(viewModel.mediaUris.size) { index ->
                        val uri = viewModel.mediaUris[index]
                        when {
                            uri.toString().contains("image") -> {
                                Image(
                                    painter = rememberAsyncImagePainter(uri),
                                    contentDescription = "Selected media",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(4.dp)
                                )
                            }

                            uri.toString().contains("video") -> {
                                Text("Video selected: $uri")
                            }

                            else -> {
                                Text("Audio selected: $uri")
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier,
                Arrangement.SpaceBetween
            ) {
                Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text("image")
                }
                Button(onClick = { videoPickerLauncher.launch("video/*") }) {
                    Text("video")
                }
                Button(onClick = { audioPickerLauncher.launch("audio/*") }) {
                    Text("audio")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.submitPost(username) { success, errorMsg ->
                            if (success) {
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("Post uploaded successfully!")
                                    navController.navigate(ScreenName.HomeScreen.route) {
                                        popUpTo(ScreenName.HomeScreen.route) { inclusive = true }
                                    }
                                }
                            } else {
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar(errorMsg ?: "Failed to upload post!")
                                }
                            }
                        }
                    }
                },
                enabled = viewModel.description.isNotEmpty() || viewModel.mediaUris.isNotEmpty()
            ) {
                Text("Post")
            }
        }
    }
}
