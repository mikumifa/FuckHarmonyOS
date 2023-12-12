package com.example.chatdiary2.ui.view.settings.appearance

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatdiary2.ui.nav.Action
import com.example.chatdiary2.ui.theme.AppTheme
import com.example.chatdiary2.ui.theme.ChatDiaryTheme
import com.example.chatdiary2.ui.theme.ThemeViewModel
import com.example.chatdiary2.ui.theme.UiPreferences
import com.example.chatdiary2.ui.view.common.AppThemesList
import com.example.chatdiary2.ui.view.common.ImageTextContent
import com.example.chatdiary2.ui.view.login.LoadingComponent
import com.example.chatdiary2.ui.view.login.ResultDialog
import com.example.chatdiary2.ui.view.profile.ProfileStyledText
import com.example.chatdiary2.util.secure.PreferenceStore


@SuppressLint("StateFlowValueCalledInComposition", "UnrememberedMutableState")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppearanceScreen(action: Action, viewModel: AppearanceViewModel = hiltViewModel()) {


    val isLoading = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val dialogMessage = remember { mutableStateOf("") }
    val dialogTitle = remember { mutableStateOf("") }
    var nowAppTheme by mutableStateOf(AppTheme.valueOf(viewModel.appThemeStateFlow.value))
    val nowColorScheme by mutableStateOf(MaterialTheme.colorScheme)

    ResultDialog(showDialog, dialogMessage.value, dialogTitle.value) {}
    if (isLoading.value) {
        LoadingComponent()
    }
    var refreshState by remember { mutableStateOf(0) }

    Scaffold(modifier = Modifier.semantics {
        testTagsAsResourceId = true
    },
        containerColor = nowColorScheme.background,
        contentColor = nowColorScheme.onBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "外观和语言", style = MaterialTheme.typography.titleLarge)
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
        Column(modifier = Modifier.padding(padding)) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = nowColorScheme.onBackground.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "修改主题", style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = nowColorScheme.primary
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            AppThemesList(
                currentTheme = nowAppTheme, amoled = false, onItemClick = {
                    viewModel.uiPreferences.appTheme().set(it.name)
                    nowAppTheme = AppTheme.valueOf(it.name)
                }, uiPreference = viewModel.uiPreferences
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = nowColorScheme.onBackground.copy(alpha = 0.4f)
            )

        }

    }
}

