package com.example.chatdiary2.ui.view.settings.appearance

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatdiary2.R
import com.example.chatdiary2.ui.theme.AppTheme
import com.example.chatdiary2.ui.view.common.AppLanguageList
import com.example.chatdiary2.ui.view.common.AppThemesList
import com.example.chatdiary2.ui.view.common.SettingItem
import com.example.chatdiary2.ui.view.main.login.LoadingComponent
import com.example.chatdiary2.ui.view.main.login.ResultDialog
import com.example.chatdiary2.ui.view.nav.Action
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.merge


@SuppressLint("StateFlowValueCalledInComposition", "UnrememberedMutableState")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppearanceScreen(action: Action, viewModel: AppearanceViewModel = hiltViewModel()) {


    val isLoading = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val dialogMessage = remember { mutableStateOf("") }
    val dialogTitle = remember { mutableStateOf("") }
    var nowAppTheme by mutableStateOf(AppTheme.valueOf(viewModel.appThemeStateFlow.value))
    val context = LocalContext.current

    //flow老监听2者的变化
    LaunchedEffect(Unit) {
        merge(
            viewModel.uiPreferences.appTheme().changes(),
            viewModel.uiPreferences.themeDarkAmoled().changes()
        )
            .drop(2)
            .collectLatest { (context as? Activity)?.let { ActivityCompat.recreate(it) } }
    }
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
                        text = stringResource(id = R.string.appearance_language),
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
        Column(modifier = Modifier.padding(padding)) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )
            SettingItem {
                Row {
                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = stringResource(id = R.string.change_theme), style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                AppThemesList(
                    currentTheme = nowAppTheme, amoled = false,
                    onItemClick = {
                        viewModel.uiPreferences.appTheme().set(it.name)
                        nowAppTheme = AppTheme.valueOf(it.name)
                    },
                )
            }
            SettingItem {
                Row {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(id = R.string.change_lang), style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                AppLanguageList()
                Spacer(modifier = Modifier.width(24.dp))
            }
        }

    }
}

