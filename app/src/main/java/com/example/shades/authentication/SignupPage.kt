package com.example.shades.authentication

import AuthViewModel
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shades.MyAppNavigation.ScreenName
import com.example.shades.R
import kotlinx.coroutines.launch

@Composable
fun SignUp(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
){
    var newId by remember  { mutableStateOf("")}
    var newPass by remember  {mutableStateOf("")}

    val loginSuccess by viewModel.currentUser.collectAsState()
    val error by viewModel.error.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Navigate to HomeScreen immediately when user is set
    LaunchedEffect(loginSuccess) {
        if (loginSuccess != null) {
            navController.navigate(ScreenName.HomeScreen.route) {
                popUpTo(ScreenName.SignupPage.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Show error snackbar
    LaunchedEffect(error) {
        error?.let {
            coroutineScope.launch {
                snackBarHostState.showSnackbar("Error: $it")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NewEmailEntry(
            newEmail= R.string.signId,
            value = newId,
            onValueChange = { newId = it },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(10.dp))
        NewPassEntry(
            pass = R.string.signPass,
            value = newPass,
            onValueChange = { newPass = it },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(10.dp))
        FilledTonalButton(
            onClick = { viewModel.signUp(newId, newPass) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.signup))
        }
        Spacer(modifier = Modifier.padding(10.dp))
        SnackbarHost(hostState = snackBarHostState)
    }
}


@Composable
fun NewEmailEntry(
    @StringRes newEmail: Int,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier
){
    TextField(
        label = {Text(stringResource(id = newEmail))},
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        modifier = modifier,
    )
}

@Composable
fun NewPassEntry(
    @StringRes pass: Int,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier
){
    TextField(
        label = {Text(stringResource(id = pass))},
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        modifier = modifier,
    )
}