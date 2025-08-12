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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shades.R
import com.example.shades.MyAppNavigation.ScreenName

@Composable

fun LogIn(
    navController: NavController
){
    var id by remember  { mutableStateOf("")}
    var pass by remember  {mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailEntry(
            email = R.string.logId,
            value = id,
            onValueChange = {id = it},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()

        )
        Spacer(modifier = Modifier.padding(10.dp))
        PassEntry(
            pass = R.string.logPass,
            value = pass,
            onValueChange = {pass = it},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(10.dp))
        FilledTonalButton(onClick = {navController.navigate(ScreenName.HomeScreen.route)}) {
            Text(text = stringResource(id = R.string.login))
        }
        Spacer(modifier = Modifier.padding(10.dp))
        FilledTonalButton(onClick = {navController.navigate(ScreenName.SignupPage.route)}) {
            Text(text = stringResource(id = R.string.signup))
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
){
    TextField(
        label = {Text(stringResource(id = email))},
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        modifier = modifier,
    )
}

@Composable
fun PassEntry(
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