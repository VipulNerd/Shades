package com.example.shades.authentication

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shades.R
import com.example.shades.MyAppNavigation.ScreenName
import kotlinx.coroutines.launch

@Composable
fun LogIn(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    var id by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val error by viewModel.error.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // ✅ Navigate only when login succeeded AND user profile is loaded
    LaunchedEffect(loginSuccess, currentUser) {
        if (loginSuccess && currentUser != null) {
            navController.navigate(ScreenName.HomeScreen.route) {
                popUpTo(ScreenName.LoginPage.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Show error snackbar
    LaunchedEffect(error) {
        error?.let {
            coroutineScope.launch { snackBarHostState.showSnackbar(it) }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailEntry(
                email = R.string.logId,
                value = id,
                onValueChange = { id = it },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            PassEntry(
                pass = R.string.logPass,
                value = pass,
                onValueChange = { pass = it },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            FilledTonalButton(
                onClick = { viewModel.login(id, pass) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(10.dp))

            FilledTonalButton(
                onClick = { navController.navigate(ScreenName.SignupPage.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Signup")
            }
        }
    }
}

@Composable
fun EmailEntry(
    @StringRes email: Int,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier
) {
    TextField(
        label = { Text(text = stringResource(id = email)) },
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        modifier = modifier
    )
}

@Composable
fun PassEntry(
    @StringRes pass: Int,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier
) {
    TextField(
        label = { Text(text = stringResource(id = pass)) },
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        modifier = modifier
    )
}