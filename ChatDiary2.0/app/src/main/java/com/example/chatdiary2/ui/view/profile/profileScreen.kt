package com.example.chatdiary2.ui.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.navigation.compose.rememberNavController
import com.example.chatdiary2.nav.Action
import com.example.chatdiary2.ui.view.login.ButtonComponent
import com.example.chatdiary2.ui.view.login.LoadingComponent
import com.example.chatdiary2.ui.view.login.NormalTextField
import com.example.chatdiary2.ui.view.login.ResultDialog


const val email = "damahecode"
const val my_description =
    "A group of simple, open source Android apps without ads and unnecessary permissions, with materials design UI."

@Composable
@Preview
@ExperimentalMaterial3Api
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    val actions = remember(navController) {
        Action(navController)
    }
    profileScreen(actions)
}

@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class
)
@Composable
@ExperimentalMaterial3Api
fun profileScreen(
    action:Action,
    profileViewModel: ProfileScreenViewModel = hiltViewModel(),
) {
    val showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val isLoading = remember { mutableStateOf(false) }

    val textState = remember { mutableStateOf("") }
    val editTitleState = remember { mutableStateOf("") }
    val onClickState = remember { mutableStateOf({}) }
    val showDialog = remember { mutableStateOf(false) }
    val dialogMessage = remember { mutableStateOf("") }
    val dialogTitle = remember { mutableStateOf("") }

    ResultDialog(showDialog, dialogMessage.value, dialogTitle.value) {

    }
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
                    Text(text = "Profile")
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
        ProfileContent(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .imePadding()

        ) {
            TopProfileLayout()
            ImageTextContent(icon = {
                Icon(Icons.Filled.Edit, contentDescription = "edit username")
            },
                text = { ProfileStyledText(text = "修改名字") },
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    editTitleState.value = "修改名字"
                    textState.value = ""
                    showBottomSheet.value = true
                    onClickState.value = {
                        isLoading.value = true
                        val result = profileViewModel.editUserName(
                            textState.value
                        );
                        result.observe(lifecycleOwner) {
                            if (it.second) {
                                isLoading.value = false
                                showDialog.value = true
                                dialogMessage.value = it.first
                                dialogTitle.value = "成功"
                            } else {
                                isLoading.value = false
                                showDialog.value = true
                                dialogMessage.value = it.first
                                dialogTitle.value = "失败"
                            }

                        }
                    }
                }

            )

            ImageTextContent(icon = {
                Icon(Icons.Filled.Edit, contentDescription = "edit sentiment")
            },
                text = { ProfileStyledText(text = "修改个性签名") },
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    editTitleState.value = "修改个性签名"
                    textState.value = ""
                    showBottomSheet.value = true
                    onClickState.value = {
                        isLoading.value = true
                        val result = profileViewModel.editUserInfo(
                            textState.value
                        );
                        result.observe(lifecycleOwner) {
                            if (it.second) {
                                isLoading.value = false
                                showDialog.value = true
                                dialogMessage.value = it.first
                                dialogTitle.value = "成功"
                            } else {
                                isLoading.value = false
                                showDialog.value = true
                                dialogMessage.value = it.first
                                dialogTitle.value = "失败"
                            }

                        }
                    }
                }

            )

            ImageTextContent(icon = {
                Icon(Icons.Filled.Edit, contentDescription = "edit password")
            },
                text = { ProfileStyledText(text = "修改密码") },
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    editTitleState.value = "修改密码"
                    textState.value = ""
                    showBottomSheet.value
                    onClickState.value = {
                        isLoading.value = true
                        val result = profileViewModel.editUserPassword(
                            textState.value
                        );
                        result.observe(lifecycleOwner) {
                            if (it.second) {
                                isLoading.value = false
                                showDialog.value = true
                                dialogMessage.value = it.first
                                dialogTitle.value = "成功"
                            } else {
                                isLoading.value = false
                                showDialog.value = true
                                dialogMessage.value = it.first
                                dialogTitle.value = "失败"
                            }

                        }
                    }
                }

            )


        }
        BottomSheetEditor(
            showBottomSheet = showBottomSheet,
            textState = textState,
            editTitleState = editTitleState,
            onClick = onClickState.value,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetEditor(
    showBottomSheet: MutableState<Boolean>,
    textState: MutableState<String>,
    editTitleState: MutableState<String>,
    onClick: () -> Unit,
) {

    if (showBottomSheet.value) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet.value = false
            },
            sheetState = sheetState, modifier = Modifier.imePadding(),

            ) {
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                ProfileStyledText(text = editTitleState.value)
            }
            Spacer(modifier = Modifier.height(40.dp))
            Box(modifier = Modifier.padding(24.dp)) {
                NormalTextField(headImageVec = Icons.Filled.Edit,
                    label = "输入你的修改",
                    textContent = textState,
                    onChange = { textState.value = it })
            }
            Spacer(modifier = Modifier.height(40.dp))
            Box(modifier = Modifier.padding(start = 48.dp, end = 48.dp)) {
                ButtonComponent(enable = false, value = "点击修改", onClick = onClick)
            }
            Spacer(modifier = Modifier.height(80.dp))

        }
    }
}

@Composable
private fun ProfileStyledText(
    text: String, color: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Text(
        text = text, style = TextStyle(
            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color
        )
    )
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier, content: @Composable () -> Unit
) {
    Column(modifier) {
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopProfileLayout() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(8),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {


            Icon(
                imageVector = Icons.Filled.Paid,
                contentDescription = "Icons",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = "app_name",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = my_description,
                style = MaterialTheme.typography.bodySmall,
            )
        }

    }
}

@Composable
fun ImageTextContent(
    icon: @Composable () -> Unit, text: @Composable () -> Unit, onClick: () -> Unit, // 点击事件的回调
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(4.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .height(48.dp)
            .clickable { onClick() }, // 添加点击事件
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(modifier = Modifier.width(8.dp))
        text()
    }
}


@Composable
fun profileListItem(
    icon: @Composable () -> Unit,
    info: String,
) {
    Row(
        modifier = Modifier.padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        ) {
            Text(
                text = info, style = MaterialTheme.typography.labelLarge
            )
        }
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = null,
            modifier = Modifier.padding(4.dp)
        )
    }
}
