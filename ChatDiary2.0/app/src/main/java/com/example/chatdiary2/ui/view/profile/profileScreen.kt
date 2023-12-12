package com.example.chatdiary2.ui.view.profile

import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.chatdiary2.data.UserVO
import com.example.chatdiary2.ui.nav.Action
import com.example.chatdiary2.ui.view.common.ImageTextContent
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
    action: Action,
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
    val userVOState = remember {
        mutableStateOf(
            UserVO(
                email = "加载中",
                username = "加载中",
                id = 0L,
                userInfo = "加载中",
                avatarUrl = null
            )
        )
    }
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
                    Text(text = "个人信息")
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

            val userVOLiveData = profileViewModel.getUserInfo()
            userVOLiveData.observe(lifecycleOwner) {
                it?.let {
                    userVOState.value = it
                    if (userVOState.value.userInfo == null) {
                        userVOState.value.userInfo = "签名啥也没写哦。"
                    }
                }
                if (it == null) {
                    showDialog.value = true
                    dialogMessage.value = "加载用户信息失败"
                    dialogTitle.value = "失败"
                }
            }
            TopProfileLayout(userVOState.value) { filePath ->
                Log.d("myPicTAG", "ImagePicker: $filePath")
                val res = profileViewModel.uploadImage(
                    uri = filePath,
                )
                res.observe(lifecycleOwner) {
                    if (it!=null) {
                        showDialog.value = true
                        dialogMessage.value = it
                        dialogTitle.value = "结果"
                    } else {
                        showDialog.value = true
                        dialogMessage.value = "上传图片失败"
                        dialogTitle.value = "失败"
                    }
                }
            }
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
                ButtonComponent(enable = true, value = "点击修改", onClick = onClick)
            }
            Spacer(modifier = Modifier.height(80.dp))

        }
    }
}

@Composable
fun ProfileStyledText(
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
fun TopProfileLayout(
    user: UserVO, sendMessage: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val imagePickerForImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()

    ) {
        if (it != null) {
            val projection = arrayOf(MediaStore.MediaColumns.DATA)
            val cursor = context.contentResolver.query(it!!, projection, null, null, null)
            val columnIndex = cursor?.getColumnIndex(MediaStore.MediaColumns.DATA)
            val filePath = try {
                if (cursor != null) {
                    cursor.moveToFirst()
                    val filePath = columnIndex?.let { cursor.getString(it) }
                    cursor.close()
                    filePath!!
                } else ""
            } catch (exception: Exception) {
                ""
            }
            sendMessage(filePath)
        }


    }


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(8),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            val imageUrl =
                if (user.avatarUrl == null) "https://gitee.com/misakabryant/chat-diary-fig/raw/master/ChatDiary/1701202402704.jpg"
                else user.avatarUrl
            AsyncImage(model = imageUrl,
                contentDescription = imageUrl,
                modifier = Modifier
                    .height(200.dp)
                    .padding(end = 2.dp, start = 2.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {

                        imagePickerForImage.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    })

            Text(
                text = user.username,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .align(Alignment.CenterHorizontally),
                text = user.email,
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = user.userInfo!!,
                style = MaterialTheme.typography.bodySmall,
            )
        }

    }
}

