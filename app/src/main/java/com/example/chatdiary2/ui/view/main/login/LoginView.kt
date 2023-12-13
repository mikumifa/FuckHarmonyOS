package com.example.chatdiary2.ui.view.main.login

import UserPref
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatdiary2.R
import com.example.chatdiary2.ui.view.nav.Action
import com.example.chatdiary2.ui.view.nav.Destination
import eu.kanade.presentation.theme.colorscheme.md_theme_light_outline
import eu.kanade.presentation.theme.colorscheme.md_theme_light_shadow

@Composable
fun LoadingComponent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator() // 显示一个进度条
    }
}

@Composable
fun LoginView(
    action: Action,
    loginViewModel: LoginViewModel = hiltViewModel(),
    password: String = "",
    email: String = ""
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(28.dp)
    ) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val emailTextState = remember { mutableStateOf(email) }
        val passwordTextState = remember { mutableStateOf(password) }
        val passwordVisible = remember { mutableStateOf(false) }

        val showDialogSuccess = remember { mutableStateOf(false) }
        val showDialogFailure = remember { mutableStateOf(false) }
        val isLoading = remember { mutableStateOf(false) }
        ResultDialog(
            showDialogSuccess,
            stringResource(R.string.login_success),
            stringResource(R.string.title_success)
        ) {
            action.toMain() {
                popUpTo(Destination.Login) {
                    inclusive = true
                }

            }
        }
        ResultDialog(
            showDialogFailure,
            stringResource(R.string.login_failed),
            stringResource(R.string.title_failed)
        ) {}
        if (isLoading.value) {
            LoadingComponent()
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LoginHead(title = stringResource(R.string.login_your_account))

            Spacer(modifier = Modifier.height(10.dp))
            NormalTextField(
                headImageVec = Icons.Filled.Email, stringResource(R.string.email), emailTextState
            ) {
                emailTextState.value = it
            }
            Spacer(modifier = Modifier.height(10.dp))
            PasswordField(passwordTextState, passwordVisible) {
                passwordTextState.value = it
            }
            Spacer(modifier = Modifier.weight(1f))
            ButtonComponent(!isLoading.value, value = stringResource(R.string.login)) {
                isLoading.value = true
                val loginUser =
                    loginViewModel.loginUser(emailTextState.value, passwordTextState.value)
                loginUser.observe(lifecycleOwner) {
                    it?.let {
                        UserPref.savePassword(
                            it.id, emailTextState.value, passwordTextState.value, context
                        )
                        isLoading.value = false

                        showDialogSuccess.value = true
                    } ?: run {
                        isLoading.value = false
                        showDialogFailure.value = true
                    }

                }


            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = stringResource(R.string.dont_have_account),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    ),
                )
                val rightText = stringResource(R.string.register)
                ClickableText(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        pushStringAnnotation(tag = rightText, annotation = rightText)
                        append(rightText)
                    }
                }, style = TextStyle(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                ), onClick = {
                    action.toRegister() {
                        popUpTo(Destination.Login) {
                            inclusive = true
                        }

                    }
                })
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }


}

@Composable
fun ResultDialog(
    showDialog: MutableState<Boolean>, message: String, resultTitle: String, action: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(onDismissRequest = { showDialog.value = false },
            title = { Text(resultTitle) },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = {
                    showDialog.value = false
                    action()
                }) {
                    Text("OK")
                }
            })
    }
}


@Composable
fun RegisterView(
    action: Action, loginViewModel: LoginViewModel = hiltViewModel()
) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(28.dp)
    ) {
        val lifecycleOwner = LocalLifecycleOwner.current
        val nameTextState = remember { mutableStateOf("") }
        val emailTextState = remember { mutableStateOf("") }
        val passwordTextState = remember { mutableStateOf("") }
        val passwordVisible = remember { mutableStateOf(false) }
        val showDialogSuccess = remember { mutableStateOf(false) }
        val showDialogSuccessMessage = remember { mutableStateOf("") }
        val showDialogFailure = remember { mutableStateOf(false) }
        val showDialogFailureMessage = remember { mutableStateOf("") }
        val isLoading = remember { mutableStateOf(false) }
        if (isLoading.value) {
            LoadingComponent()
        }
        ResultDialog(
            showDialogSuccess,
            showDialogSuccessMessage.value,
            stringResource(R.string.title_success)
        ) {
            action.toLogin() {
                popUpTo(action.navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
        ResultDialog(
            showDialogFailure, showDialogFailureMessage.value, stringResource(R.string.title_failed)
        ) {

        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LoginHead(title = stringResource(R.string.create_account))
            Spacer(modifier = Modifier.height(10.dp))
            NormalTextField(Icons.Filled.AccountBox, stringResource(R.string.name), nameTextState) {
                nameTextState.value = it
            }
            Spacer(modifier = Modifier.height(10.dp))
            NormalTextField(Icons.Filled.Email, stringResource(R.string.email), emailTextState) {
                emailTextState.value = it
            }
            Spacer(modifier = Modifier.height(10.dp))
            PasswordField(passwordTextState, passwordVisible) {
                passwordTextState.value = it
            }
            Spacer(modifier = Modifier.height(200.dp))
            Spacer(modifier = Modifier.weight(1f))
            ButtonComponent(!isLoading.value, value = stringResource(R.string.register)) {
                isLoading.value = true
                val result = loginViewModel.registerUser(
                    nameTextState.value, passwordTextState.value, emailTextState.value
                );
                result.observe(lifecycleOwner) {
                    if (it.second) {
                        isLoading.value = false
                        showDialogSuccess.value = true
                        showDialogSuccessMessage.value = it.first
                    } else {
                        isLoading.value = false
                        showDialogFailure.value = true
                        showDialogFailureMessage.value = it.first
                    }

                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = stringResource(R.string.have_account),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    ),
                )
                val rightText = "登录"
                ClickableText(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        pushStringAnnotation(tag = rightText, annotation = rightText)
                        append(rightText)
                    }
                }, style = TextStyle(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                ), onClick = {
                    action.toLogin() {
                        popUpTo(Destination.Register) {
                            inclusive = true
                        }
                    }
                })
            }
            Spacer(modifier = Modifier.height(80.dp))

        }
    }


}


@Composable
fun LoginHead(title: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp),
            text = stringResource(R.string.hello),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 24.sp, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Normal
            ),
            color = md_theme_light_outline
        )
        Text(
            text = title, modifier = Modifier
                .fillMaxWidth()
                .heightIn(), style = TextStyle(
                fontSize = 30.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal
            ), color = md_theme_light_shadow, textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    password: MutableState<String>,
    passwordVisible: MutableState<Boolean>,
    onChange: (String) -> Unit,
) {
    val localFocusManager = LocalFocusManager.current


    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small),
        label = { Text(text = stringResource(R.string.password)) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
        ),
        singleLine = true,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()

        },
        maxLines = 1,
        value = password.value,
        onValueChange = onChange,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_password_24),
                contentDescription = stringResource(R.string.password)
            )
        },
        trailingIcon = {
            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }

            val description = if (passwordVisible.value) {
                "隐藏密码"
            } else {
                "显示密码"
            }

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }
        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None
        else PasswordVisualTransformation()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NormalTextField(
    headImageVec: ImageVector,
    label: String,
    textContent: MutableState<String>,
    onChange: (String) -> Unit
) {
    val localFocusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small),
        label = { Text(text = label) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = if (label == stringResource(R.string.email)) KeyboardType.Email else KeyboardType.Ascii,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()

        },
        maxLines = 1,
        value = textContent.value,
        onValueChange = onChange,
        leadingIcon = {
            Icon(
                imageVector = headImageVec, contentDescription = label
            )
        },

        )
}


@Composable
fun ButtonComponent(enable: Boolean, value: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enable,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.primary
                        )
                    ), shape = RoundedCornerShape(50.dp)
                ), contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                style = TextStyle(color = MaterialTheme.colorScheme.onPrimary)
            )
        }
    }
}