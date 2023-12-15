package com.example.chatdiary.ui.view.settings.lock

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.chatdiary.R
import com.example.chatdiary.ui.view.common.SettingStyledText
import com.example.chatdiary.ui.view.common.SwitchBottom
import com.example.chatdiary.ui.view.main.login.LoadingComponent
import com.example.chatdiary.ui.view.main.login.ResultDialog
import com.example.chatdiary.ui.view.nav.Action


@SuppressLint("ResourceType", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LockScreen(action: Action, lockScreenViewModel: LockScreenViewModel? = hiltViewModel()) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val isLoading = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val dialogMessage = remember { mutableStateOf("") }
    val dialogTitle = remember { mutableStateOf("") }
    ResultDialog(showDialog, dialogMessage.value, dialogTitle.value) {}
    if (isLoading.value) {
        LoadingComponent()
    }
    Scaffold(modifier = Modifier.semantics {
        testTagsAsResourceId = true
    },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.device_lock),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        action.navController.navigateUp()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        }) { padding ->
        Column(modifier = Modifier.padding(paddingValues = padding)) {
            Spacer(modifier = Modifier.height(10.dp))
            Image(
                painter = painterResource(id = R.raw.lock),
                contentDescription = "锁图标",
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.device_lock_description_part1),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary, fontSize = 24.sp
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(id = R.string.device_lock_description_part2),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary, fontSize = 24.sp
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(2.dp)
                    )
            ) {
                SwitchBottom(
                    icon = {
                        Icon(Icons.Filled.Lock, contentDescription = "edit sentiment")
                    },
                    text = {
                        SettingStyledText(stringResource(id = R.string.device_lock_setting))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        lockScreenViewModel!!.setuseAuthenticator(it)
                    },
                    initState = lockScreenViewModel!!.useAuthenticator.value

                )
            }


        }


    }

}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
@Preview
fun LockScreenPreview() {

    val navController = rememberNavController()
    val action = remember(navController) {
        Action(navController)

    }
    LockScreen(action, lockScreenViewModel = null)
}