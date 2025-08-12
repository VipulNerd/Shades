package com.example.shades.authentication

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shades.MyAppNavigation.ScreenName
import com.example.shades.R

@Composable
fun SignUp(
    navController: NavController
){
    var newId by remember  { mutableStateOf("")}
    var newPass by remember  {mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NewEmailEntry(
            newEmail= R.string.signId,
            value = newId,
            onValueChange = { newId = it },
            keyboardOptions = KeyboardOptions.Default.copy(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(10.dp))
        NewPassEntry(
            pass = R.string.signPass,
            value = newPass,
            onValueChange = { newPass = it },
            keyboardOptions = KeyboardOptions.Default.copy(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(10.dp))
        FilledTonalButton(onClick = {navController.navigate(ScreenName.HomeScreen.route)}) {
            Text(stringResource(id = R.string.signup))
        }
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