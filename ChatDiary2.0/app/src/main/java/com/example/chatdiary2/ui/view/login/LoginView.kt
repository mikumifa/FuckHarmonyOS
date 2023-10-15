package com.example.chatdiary2.ui.view.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.chatdiary2.R
import com.example.chatdiary2.nav.Action
import com.example.chatdiary2.ui.view.login.common.ButtonComponent
import com.example.chatdiary2.ui.view.login.common.LoginHead
import com.example.chatdiary2.ui.view.login.common.NormalTextField
import com.example.chatdiary2.ui.view.login.common.PasswordField

@Composable
fun LoginView(
    action: Action
) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LoginHead(title = stringResource(R.string.login_your_acount))

            Spacer(modifier = Modifier.height(10.dp))
            NormalTextField(headImageVec = Icons.Filled.Email, stringResource(R.string.email))
            Spacer(modifier = Modifier.height(10.dp))
            PasswordField()
            Spacer(modifier = Modifier.weight(1f))
            ButtonComponent(value = stringResource(id = R.string.Login)) {
                
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = stringResource(id = R.string.Dont_have_acount),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    ),
                )
                val rightText = stringResource(id = R.string.register)
                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            pushStringAnnotation(tag = rightText, annotation = rightText)
                            append(rightText)
                        }
                    },
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal
                    ),
                    onClick = {
                        action.toRegister()
                    }
                )
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }


}


@Preview
@Composable
fun LoginViewPreview() {
    val navController = rememberNavController()
    val actions = remember(navController) {
        Action(navController)
    }
    LoginView(action = actions)
}