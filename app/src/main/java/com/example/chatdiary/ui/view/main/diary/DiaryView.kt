package com.example.chatdiary.ui.view.main.diary

import EncryptionUtils
import SpeechToTextUtil
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.IndeterminateCheckBox
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdiary.R
import com.example.chatdiary.data.Diary
import com.example.chatdiary.ui.view.common.HorizontalImageList
import com.example.chatdiary.ui.view.common.RequestWithPermission
import com.example.chatdiary.ui.view.common.ZoomableImage
import com.example.chatdiary.ui.view.common.keyboardAsState
import com.example.chatdiary.ui.view.nav.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TimedDialogPreview() {
    ErrorDialog("萨达 萨达大大打多少啊打算的阿三的")

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopBarPreview() {

    TopAppBar(title = {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Diary", maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            val currentDate = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date())
            Text(
                currentDate,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )
        }

    })
}

@Composable
fun ErrorDialog(text: String) {
    Dialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.errorContainer)
                .width(200.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "",
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .align(Alignment.CenterHorizontally),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = text,
                fontSize = 25.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}


enum class InputSelector {
    NONE, MAP, EMOJI, IMAGE
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryView(
    action: Action,
    diaryViewModel: DiaryViewModel? = hiltViewModel(),
    myDate: LocalDate = LocalDate.now(),
    haveInputDialog: Boolean = true,
) {
    val context = LocalContext.current
    var lifecycleOwner = LocalLifecycleOwner.current
    val encryptionUtils = EncryptionUtils(context)
    var searchText by remember { mutableStateOf("") }
    val diaryList = remember { mutableStateOf(emptyList<Diary>()) }
    var hasSearchResult by remember { mutableStateOf(false) }
    var picUrl by remember { mutableStateOf<String?>(null) }
    // 创建 LazyListState 来监听滚动
    val listState = rememberLazyListState()
    //创建一个只在 listState.firstVisibleItemIndex 改变时更新的派生状态
    val showButton = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }.value
    val scope = rememberCoroutineScope()
    Box {

        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ), title = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        "日记", maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    val currentDate =
                        SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date())
                    Text(
                        currentDate,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }, navigationIcon = {
                IconButton(onClick = { action.navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }, actions = {
                Spacer(modifier = Modifier.size(40.dp))
            })

        }, content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(paddingValues = paddingValues)
            ) {
                Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceVariant)) {
                    Search(searchText, onQueryChange = { searchText = it }, onSearch = {
                        val searchDiary =
                            diaryViewModel?.searchDiariesByKeywordFlowAndDate(searchText, myDate)
                        searchDiary?.observe(lifecycleOwner) {
                            diaryList.value = it
                            hasSearchResult = true
                        }
                    }, onCancel = {
                        hasSearchResult = false
                        searchText = ""
                    })
                }
                Box(
                    Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    LazyColumn(
                        state = listState, modifier = Modifier.fillMaxSize()
                    ) {

                        val diary = diaryViewModel?.searchDiariesByDateFlow(myDate)
                        diary!!.observe(lifecycleOwner) {
                            if (!hasSearchResult) diaryList.value = it
                        }
                        items(diaryList.value.size) {
                            val item = diaryList.value[it]
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
                            dateFormat.parse(item.timestamp)?.let { date ->
                                DiaryItem(title = item.title,
                                    context = item.content,
                                    pos = item.position,
                                    type = item.type,
                                    imageUrls = item.images,
                                    time = date,
                                    onClickImage = { url ->
                                        picUrl = url
                                    })
                            }
                        }

                    }

                    if (showButton) {
                        FloatingActionButton(
                            onClick = {                // 实现回到顶部的逻辑
                                scope.launch {
                                    listState.animateScrollToItem(0)
                                }

                            }, modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                        ) {
                            Icon(Icons.Filled.ArrowUpward, contentDescription = "Go to Top")
                        }
                    } //
                }
                if (haveInputDialog) {
                    InputDialog(
                        diaryViewModel!!, onSent = {
                            val diary = diaryViewModel.searchDiariesByDateFlow(myDate)
                            diary.observe(lifecycleOwner) {
                                if (!hasSearchResult) diaryList.value = it
                            }
                        }, actions = action
                    )

                }
            }
        })
        //下面覆盖
        if (picUrl != null) {
            ZoomableImage(imageUrl = picUrl!!,
                modifier = Modifier.fillMaxSize(),
                onClose = { picUrl = null })
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onCancel: () -> Unit,

    ) {
    val localFocusManager = LocalFocusManager.current
    var isExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 8.dp, end = 8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(value = query,
            onValueChange = { newQuery ->
                onQueryChange(newQuery)
            },
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.primaryContainer),
            label = {
                Text(
                    text = "Search...",
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                onSearch()
                localFocusManager.clearFocus()
            }),
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            trailingIcon = {
                if (query.isNotEmpty()) {
                    Icon(imageVector = Icons.Default.Cancel,
                        contentDescription = "Cancel",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.clickable {
                            onCancel()
                        })
                } else {
                    Icon(imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,

                        modifier = Modifier.clickable {
                            onSearch()
                        })
                }
            })

    }
}


@Composable
fun TimedDialog(
    showDialog: MutableState<Boolean>, durationMillis: Long, text: String, onDismiss: () -> Unit
) {
    val visible = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(showDialog.value) {
        if (showDialog.value) {
            visible.value = true
            delay(durationMillis)
            visible.value = false
            showDialog.value = false
            onDismiss()
        }
    }

    if (visible.value) {
        ErrorDialog(text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDialog(
    diaryViewModel: DiaryViewModel, onSent: () -> Unit, actions: Action
) {
    val context = LocalContext.current
    var text by remember { mutableStateOf(TextFieldValue()) }
    val isSending = remember { mutableStateOf(false) }
    val area = rememberSaveable { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }
    val isErrorShow = remember { mutableStateOf(false) }
    var errorShowInfo by remember { mutableStateOf("") }
    val speechToTextUtil = SpeechToTextUtil(LocalContext.current)
    val isToolbarShow = remember { mutableStateOf(false) }
    val isKeyboardOpen by keyboardAsState() // true or false
    val focusRequester = remember { FocusRequester() }
    var currentInputSelector by rememberSaveable { mutableStateOf(InputSelector.NONE) }
    val lifecycleOwner = LocalLifecycleOwner.current
    speechToTextUtil.setSpeechRecognitionListener(onSpeechRecognitionResult = {
        text = text.copy(text = it)
    }, onSpeechRecognitionError = {
        isErrorShow.value = true
        errorShowInfo = "语言识别失败"
    })
    RequestWithPermission(Manifest.permission.RECORD_AUDIO, onError = {
        errorShowInfo = "没有语音权限，没办法语音识别哦"
        isErrorShow.value = true
    }, onSuccess = {
        /* nothing to do*/
    })
    val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            diaryViewModel.viewModelScope.launch {
                val geocoder = Geocoder(context, Locale.getDefault())
                area.value = "unknown area"
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var selectedImageUris: List<String> by remember {
        mutableStateOf(emptyList())
    }
    val imagePickerForMultipleImages = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        if (uris.isNotEmpty()) {
            selectedImageUris = uris.map {
                val projection = arrayOf(MediaStore.MediaColumns.DATA)
                val cursor = context.contentResolver.query(it, projection, null, null, null)
                val columnIndex = cursor?.getColumnIndex(MediaStore.MediaColumns.DATA)
                try {
                    if (cursor != null) {
                        cursor.moveToFirst()
                        val filePath = columnIndex?.let { cursor.getString(it) }
                        cursor.close()
                        filePath!!
                    } else ""
                } catch (exception: Exception) {
                    ""
                }


            }
            Log.d("myPicTAG", "ImagePicker: $selectedImageUris")
            val res = diaryViewModel.uploadImage(
                position = area.value,
                uris = selectedImageUris,
            )
            res.observe(lifecycleOwner) {
                if (it == true) {
                    text = TextFieldValue()
                    isSending.value = false
                    onSent()
                } else {
                    isErrorShow.value = true
                    errorShowInfo = "发送失败"
                }
            }
        } else {
            Log.w("myPicTAG", "ImagePicker: No Selected Images")
        }
    }


    TimedDialog(showDialog = isErrorShow,
        durationMillis = 1000,
        text = errorShowInfo,
        onDismiss = {})

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            val lifecycleOwner = LocalLifecycleOwner.current
            if (!isKeyboardOpen) {
                IconButton(onClick = {
                    isToolbarShow.value = !isToolbarShow.value
                    if (!isToolbarShow.value) {
                        currentInputSelector = InputSelector.NONE
                    }
                }) {
                    Icon(
                        imageVector = if (isToolbarShow.value) Icons.Filled.IndeterminateCheckBox else Icons.Filled.AddBox,
                        contentDescription = "send",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            OutlinedTextField(value = text, onValueChange = {
                text = it
                isSending.value = it.text.isNotBlank()
            }, singleLine = false, modifier = Modifier
                .weight(1f)
                .onFocusChanged {
                    if (it.isFocused) {
                        isToolbarShow.value = false
                    }
                    currentInputSelector = InputSelector.NONE
                }
                .padding(4.dp),

                shape = RoundedCornerShape(18.dp), colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ))
            if (isSending.value) {
                IconButton(
                    onClick = {
                        val res = diaryViewModel.addDiary(
                            position = area.value, content = text.text
                        )
                        res.observe(lifecycleOwner) {
                            if (it == true) {
                                text = TextFieldValue()
                                isSending.value = false
                                onSent()
                            } else {
                                isErrorShow.value = true
                                errorShowInfo = "发送失败"
                            }
                        }
                    }, modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_send_24),
                        contentDescription = "send",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer

                    )
                }
            } else {
                if (speechToTextUtil.isCanUseSpeechRecognition) {
                    IconToggleButton(
                        //语音
                        checked = isRecording, onCheckedChange = { isChecked ->
                            if (isChecked) {
                                speechToTextUtil.startListening()
                            } else {
                                speechToTextUtil.stopListening()
                            }
                            isRecording = isChecked
                        }, modifier = Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = if (!isRecording) ImageVector.vectorResource(R.drawable.baseline_keyboard_voice_24)
                            else ImageVector.vectorResource(R.drawable.baseline_stop_24),
                            contentDescription = "keyboard voice",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
        if (isToolbarShow.value) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            ) {
                Row {
                    IconButton(onClick = { currentInputSelector = InputSelector.EMOJI }) {
                        Icon(
                            Icons.Filled.EmojiEmotions,
                            contentDescription = "Localized description",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,

                            )

                    }
                    IconButton(onClick = {
                        imagePickerForMultipleImages.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )

                    }) {
                        Icon(
                            Icons.Filled.Image,
                            contentDescription = "Localized description",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }

            SelectorExpanded(
                onTextAdded = {
                    val currentText = text.text
                    val cursorPosition = text.selection.end
                    val newText =
                        currentText.substring(0, cursorPosition) + it + currentText.substring(
                            cursorPosition
                        )
                    text = text.copy(
                        text = newText, selection = TextRange(cursorPosition + it.length)
                    )
                    isSending.value = it.isNotBlank()
                },
                currentSelector = currentInputSelector,
            )
        }
    }
}

@Composable
fun DiaryItem(
    title: String,
    context: String,
    pos: String,
    type: String,
    imageUrls: List<String>?,
    time: Date,
    onClickImage: (String) -> Unit

) {
    val scrollState = rememberScrollState()
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ), modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .requiredHeightIn(max = 300.dp) // 设置最大高度为250dp
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = type,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = context, style = MaterialTheme.typography.bodyMedium
                )
                if (imageUrls != null && imageUrls.isNotEmpty()) {
                    HorizontalImageList(
                        imageUrls = imageUrls, modifier = Modifier, onClick = onClickImage
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = pos,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                    )

                    val dateFormat = SimpleDateFormat("HH:mm", Locale.CHINA)
                    Text(
                        text = dateFormat.format(time),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

enum class EmojiStickerSelector {
    EMOJI,
}

@Composable
fun ExtendedSelectorInnerButton(
    text: String, onClick: () -> Unit, selected: Boolean, modifier: Modifier = Modifier
) {
    val colors = ButtonDefaults.buttonColors(
        containerColor = if (selected) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
        else Color.Transparent,
        disabledContainerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.74f)
    )
    TextButton(
        onClick = onClick,
        modifier = modifier
            .padding(8.dp)
            .height(36.dp),
        colors = colors,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text, style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun SelectorExpanded(
    currentSelector: InputSelector,
    onTextAdded: (String) -> Unit,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    SideEffect {
        if (currentSelector == InputSelector.EMOJI) {
            keyboard?.hide()
        }
        if (currentSelector == InputSelector.IMAGE) {
            keyboard?.hide()
        }
        if (currentSelector == InputSelector.MAP) {
            keyboard?.hide()
        }
    }
    AnimatedContent(
        targetState = currentSelector, transitionSpec = {
            fadeIn().togetherWith(fadeOut())
        }, label = ""
    ) { targetState ->
        Surface(tonalElevation = 8.dp) {
            when (targetState) {
                InputSelector.EMOJI -> EmojiSelector(onTextAdded)
                InputSelector.NONE -> Spacer(
                    modifier = Modifier
                        .height(0.dp)
                        .fillMaxWidth()
                )

                else -> {
                    throw NotImplementedError()
                }
            }
        }
    }
}

@Composable
fun EmojiSelector(
    onTextAdded: (String) -> Unit
) {
    var selected by remember { mutableStateOf(EmojiStickerSelector.EMOJI) }
    Column(modifier = Modifier.semantics { contentDescription = "Emoji Selector" }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            ExtendedSelectorInnerButton(
                text = "emojis",
                onClick = { selected = EmojiStickerSelector.EMOJI },
                selected = true,
                modifier = Modifier.weight(1f)
            )

        }
        Row(modifier = Modifier.verticalScroll(rememberScrollState())) {
            EmojiTable(onTextAdded, modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun EmojiTable(
    onTextAdded: (String) -> Unit, modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        repeat(4) { x ->
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(EMOJI_COLUMNS) { y ->
                    val emoji = emojis[x * EMOJI_COLUMNS + y]
                    Text(
                        modifier = Modifier
                            .clickable(onClick = { onTextAdded(emoji) })
                            .sizeIn(minWidth = 42.dp, minHeight = 42.dp)
                            .padding(8.dp),
                        text = emoji,
                        style = LocalTextStyle.current.copy(
                            fontSize = 18.sp, textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}

private const val EMOJI_COLUMNS = 10
private val emojis = listOf(
    "\ud83d\ude00", // Grinning Face
    "\ud83d\ude01", // Grinning Face With Smiling Eyes
    "\ud83d\ude02", // Face With Tears of Joy
    "\ud83d\ude03", // Smiling Face With Open Mouth
    "\ud83d\ude04", // Smiling Face With Open Mouth and Smiling Eyes
    "\ud83d\ude05", // Smiling Face With Open Mouth and Cold Sweat
    "\ud83d\ude06", // Smiling Face With Open Mouth and Tightly-Closed Eyes
    "\ud83d\ude09", // Winking Face
    "\ud83d\ude0a", // Smiling Face With Smiling Eyes
    "\ud83d\ude0b", // Face Savouring Delicious Food
    "\ud83d\ude0e", // Smiling Face With Sunglasses
    "\ud83d\ude0d", // Smiling Face With Heart-Shaped Eyes
    "\ud83d\ude18", // Face Throwing a Kiss
    "\ud83d\ude17", // Kissing Face
    "\ud83d\ude19", // Kissing Face With Smiling Eyes
    "\ud83d\ude1a", // Kissing Face With Closed Eyes
    "\u263a", // White Smiling Face
    "\ud83d\ude42", // Slightly Smiling Face
    "\ud83e\udd17", // Hugging Face
    "\ud83d\ude07", // Smiling Face With Halo
    "\ud83e\udd13", // Nerd Face
    "\ud83e\udd14", // Thinking Face
    "\ud83d\ude10", // Neutral Face
    "\ud83d\ude11", // Expressionless Face
    "\ud83d\ude36", // Face Without Mouth
    "\ud83d\ude44", // Face With Rolling Eyes
    "\ud83d\ude0f", // Smirking Face
    "\ud83d\ude23", // Persevering Face
    "\ud83d\ude25", // Disappointed but Relieved Face
    "\ud83d\ude2e", // Face With Open Mouth
    "\ud83e\udd10", // Zipper-Mouth Face
    "\ud83d\ude2f", // Hushed Face
    "\ud83d\ude2a", // Sleepy Face
    "\ud83d\ude2b", // Tired Face
    "\ud83d\ude34", // Sleeping Face
    "\ud83d\ude0c", // Relieved Face
    "\ud83d\ude1b", // Face With Stuck-Out Tongue
    "\ud83d\ude1c", // Face With Stuck-Out Tongue and Winking Eye
    "\ud83d\ude1d", // Face With Stuck-Out Tongue and Tightly-Closed Eyes
    "\ud83d\ude12", // Unamused Face
    "\ud83d\ude13", // Face With Cold Sweat
    "\ud83d\ude14", // Pensive Face
    "\ud83d\ude15", // Confused Face
    "\ud83d\ude43", // Upside-Down Face
    "\ud83e\udd11", // Money-Mouth Face
    "\ud83d\ude32", // Astonished Face
    "\ud83d\ude37", // Face With Medical Mask
    "\ud83e\udd12", // Face With Thermometer
    "\ud83e\udd15", // Face With Head-Bandage
    "\u2639", // White Frowning Face
    "\ud83d\ude41", // Slightly Frowning Face
    "\ud83d\ude16", // Confounded Face
    "\ud83d\ude1e", // Disappointed Face
    "\ud83d\ude1f", // Worried Face
    "\ud83d\ude24", // Face With Look of Triumph
    "\ud83d\ude22", // Crying Face
    "\ud83d\ude2d", // Loudly Crying Face
    "\ud83d\ude26", // Frowning Face With Open Mouth
    "\ud83d\ude27", // Anguished Face
    "\ud83d\ude28", // Fearful Face
    "\ud83d\ude29", // Weary Face
    "\ud83d\ude2c", // Grimacing Face
    "\ud83d\ude30", // Face With Open Mouth and Cold Sweat
    "\ud83d\ude31", // Face Screaming in Fear
    "\ud83d\ude33", // Flushed Face
    "\ud83d\ude35", // Dizzy Face
    "\ud83d\ude21", // Pouting Face
    "\ud83d\ude20", // Angry Face
    "\ud83d\ude08", // Smiling Face With Horns
    "\ud83d\udc7f", // Imp
    "\ud83d\udc79", // Japanese Ogre
    "\ud83d\udc7a", // Japanese Goblin
    "\ud83d\udc80", // Skull
    "\ud83d\udc7b", // Ghost
    "\ud83d\udc7d", // Extraterrestrial Alien
    "\ud83e\udd16", // Robot Face
    "\ud83d\udca9", // Pile of Poo
    "\ud83d\ude3a", // Smiling Cat Face With Open Mouth
    "\ud83d\ude38", // Grinning Cat Face With Smiling Eyes
    "\ud83d\ude39", // Cat Face With Tears of Joy
    "\ud83d\ude3b", // Smiling Cat Face With Heart-Shaped Eyes
    "\ud83d\ude3c", // Cat Face With Wry Smile
    "\ud83d\ude3d", // Kissing Cat Face With Closed Eyes
    "\ud83d\ude40", // Weary Cat Face
    "\ud83d\ude3f", // Crying Cat Face
    "\ud83d\ude3e", // Pouting Cat Face
    "\ud83d\udc66", // Boy
    "\ud83d\udc67", // Girl
    "\ud83d\udc68", // Man
    "\ud83d\udc69", // Woman
    "\ud83d\udc74", // Older Man
    "\ud83d\udc75", // Older Woman
    "\ud83d\udc76", // Baby
    "\ud83d\udc71", // Person With Blond Hair
    "\ud83d\udc6e", // Police Officer
    "\ud83d\udc72", // Man With Gua Pi Mao
    "\ud83d\udc73", // Man With Turban
    "\ud83d\udc77", // Construction Worker
    "\u26d1", // Helmet With White Cross
    "\ud83d\udc78", // Princess
    "\ud83d\udc82", // Guardsman
    "\ud83d\udd75", // Sleuth or Spy
    "\ud83c\udf85", // Father Christmas
    "\ud83d\udc70", // Bride With Veil
    "\ud83d\udc7c", // Baby Angel
    "\ud83d\udc86", // Face Massage
    "\ud83d\udc87", // Haircut
    "\ud83d\ude4d", // Person Frowning
    "\ud83d\ude4e", // Person With Pouting Face
    "\ud83d\ude45", // Face With No Good Gesture
    "\ud83d\ude46", // Face With OK Gesture
    "\ud83d\udc81", // Information Desk Person
    "\ud83d\ude4b", // Happy Person Raising One Hand
    "\ud83d\ude47", // Person Bowing Deeply
    "\ud83d\ude4c", // Person Raising Both Hands in Celebration
    "\ud83d\ude4f", // Person With Folded Hands
    "\ud83d\udde3", // Speaking Head in Silhouette
    "\ud83d\udc64", // Bust in Silhouette
    "\ud83d\udc65", // Busts in Silhouette
    "\ud83d\udeb6", // Pedestrian
    "\ud83c\udfc3", // Runner
    "\ud83d\udc6f", // Woman With Bunny Ears
    "\ud83d\udc83", // Dancer
    "\ud83d\udd74", // Man in Business Suit Levitating
    "\ud83d\udc6b", // Man and Woman Holding Hands
    "\ud83d\udc6c", // Two Men Holding Hands
    "\ud83d\udc6d", // Two Women Holding Hands
    "\ud83d\udc8f" // Kiss
)
