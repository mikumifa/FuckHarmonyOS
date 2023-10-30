package com.example.chatdiary2.ui.view.diary

import EncryptionUtils
import SpeechToTextUtil
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.Image
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.IndeterminateCheckBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdiary2.R
import com.example.chatdiary2.data.Diary
import com.example.chatdiary2.nav.Action
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
                "Diary",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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

data class DrawerMenu(
    val icon: ImageVector, val title: String, val onClick: (actions: Action) -> Unit
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryView(
    action: Action,
    diaryViewModel: DiaryViewModel? = hiltViewModel(),
    myDate: LocalDate = LocalDate.now()
) {
    val context = LocalContext.current
    var lifecycleOwner = LocalLifecycleOwner.current
    val encryptionUtils = EncryptionUtils(context)
    val useId = encryptionUtils.decrypt("userId").toLong()
    var searchText by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var diaryList = remember { mutableStateOf(emptyList<Diary>()) }
    var hasSearchResult by remember { mutableStateOf(false) }
    var hasDateResult by remember { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(title = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Diary",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
                    contentDescription = "Localized description"
                )
            }
        }, actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.MoreHoriz,
                    contentDescription = "Localized description"
                )
            }
        })

    }, content = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(paddingValues = it)
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
                }
                )
            }
            Box(
                Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    Modifier.fillMaxSize()
                ) {

                    val diary = diaryViewModel?.searchDiariesByDateFlow(myDate)
                    diary!!.observe(lifecycleOwner) {
                        if (!hasSearchResult && !hasDateResult) diaryList.value = it
                    }
                    items(diaryList.value.size) {
                        val item = diaryList.value[it]
                        DiaryItem(
                            title = item.title,
                            context = item.content,
                            pos = item.position,
                            type = item.type,
                            imageUrls = emptyList<String>()
                        )
                    }

                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                ) {

                }
            }
            InputDialog(useId, diaryViewModel!!, onSent = {
                val diary = diaryViewModel.searchDiariesByDateFlow(myDate)
                diary.observe(lifecycleOwner) {
                    if (!hasSearchResult) diaryList.value = it
                }
            })
        }
    })


}

@Preview
@Composable
fun DatePickerPreview() {
    DatePicker({}, {})
}

@Composable
fun DatePicker(onDateSelected: (LocalDate) -> Unit, onDismissRequest: () -> Unit) {
    val selDate = remember { mutableStateOf(LocalDate.now()) }

    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    text = selDate.value.format(DateTimeFormatter.ofPattern("MMM d, YYYY")),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.size(16.dp))
            }

            CustomCalendarView(onDateSelected = {
                selDate.value = it
            })

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    //TODO - hardcode string
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                TextButton(onClick = {
                    onDateSelected(selDate.value)
                    onDismissRequest()
                }) {
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

            }
        }
    }
}

@Composable
fun DatePickerDialogButton(
    onDateSelected: (LocalDate) -> Unit, onDismissRequest: () -> Unit
) {
    var isDatePickerVisible by remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    FloatingActionButton(onClick = { isDatePickerVisible = true }, content = {
        Icon(
            imageVector = Icons.Default.DateRange, contentDescription = "Open Date Picker"
        )
    }, modifier = Modifier
        .padding(end = 8.dp, bottom = 90.dp)
        .size(70.dp)

    )
    if (isDatePickerVisible) {
        Dialog(
            onDismissRequest = {
                isDatePickerVisible = false
                onDismissRequest()
            }, properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            DatePicker(onDateSelected = {
                selectedDate.value = it
                isDatePickerVisible = false
                onDateSelected(it)
            }, onDismissRequest = {
                isDatePickerVisible = false
                onDismissRequest()
            })
        }
    }
}

@Composable
fun CustomCalendarView(onDateSelected: (LocalDate) -> Unit) {
    // Adds view to Compose
    AndroidView(modifier = Modifier.wrapContentSize(), factory = { context ->
        CalendarView(ContextThemeWrapper(context, R.style.CalenderViewCustom))
    }, update = { view ->
        view.setOnDateChangeListener { _, year, month, dayOfMonth ->
            onDateSelected(
                LocalDate.now().withMonth(month + 1).withYear(year).withDayOfMonth(dayOfMonth)
            )
        }
    })
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
                        tint = Color.White,
                        modifier = Modifier.clickable {
                            onCancel()
                        })
                } else {
                    Icon(imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White,
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
    useId: Long,
    diaryViewModel: DiaryViewModel,
    onSent: () -> Unit,
) {

    val context = LocalContext.current
    val localFocusManager = LocalFocusManager.current
    val text = rememberSaveable { mutableStateOf("") }
    val isSending = remember { mutableStateOf(false) }
    val area = rememberSaveable { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }
    var isErrorShow = remember { mutableStateOf(false) }
    var errorShowInfo by remember { mutableStateOf("") }
    val speechToTextUtil = SpeechToTextUtil(LocalContext.current)
    val isToolbarShow = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    speechToTextUtil.setSpeechRecognitionListener(onSpeechRecognitionResult = {
        text.value = it
    }, onSpeechRecognitionError = {
        isErrorShow.value = true
        errorShowInfo = "语言识别失败"
    })
    val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            diaryViewModel.viewModelScope.launch {
                val geocoder = Geocoder(context, Locale.getDefault())
                //               try {
//                    val addresses =
//                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
//
//                    if (addresses != null) {
//                        if (addresses.isNotEmpty()) {
//                            val address = addresses[0]
//                            area.value = address.locality
//                        } else {
//                            area.value = "unknown area"
//                        }
//                    }
//                } catch (e: Exception) {
//                    area.value = "unknown area"
//                }
                area.value = "unknown area"
            }
        }


        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String) {
        }

        override fun onProviderDisabled(provider: String) {
        }
    }
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val keyboardController = LocalSoftwareKeyboardController.current



    if (area.value == "") {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val requestPermissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 1000, 1.0f, locationListener
                    )
                }
            }
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 1.0f, locationListener
            )
        }
    }

    TimedDialog(showDialog = isErrorShow,
        durationMillis = 1000,
        text = errorShowInfo,
        onDismiss = {})

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            val lifecycleOwner = LocalLifecycleOwner.current
            IconButton(onClick = {
                isToolbarShow.value = !isToolbarShow.value
            }) {
                Icon(
                    imageVector = if (isToolbarShow.value) Icons.Filled.IndeterminateCheckBox else Icons.Filled.AddBox,
                    contentDescription = "send",
                )
            }
            TextField(
                value = text.value,
                onValueChange = {
                    text.value = it
                    isSending.value = it.isNotBlank()
                },

                singleLine = false,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .onFocusChanged {
                        isToolbarShow.value = it.isFocused
                        if (!it.isFocused) {
                            keyboardController?.hide()
                        }
                    },
                shape = RoundedCornerShape(18.dp),
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            if (isSending.value) {
                isToolbarShow.value = true
                IconButton(
                    onClick = {
                        val res = diaryViewModel.addDiary(
                            position = area.value, content = text.value, authorId = useId
                        )
                        res.observe(lifecycleOwner) {
                            if (it == true) {
                                text.value = ""
                                isSending.value = false
                                localFocusManager.clearFocus()
                                onSent()
                            } else {
                                isErrorShow.value = true
                                errorShowInfo = "发送失败"
                            }
                        }
                    }, modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp)
                        )
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_send_24),
                        contentDescription = "send",
                        tint = Color.White
                    )
                }
            } else {
                IconToggleButton(
                    checked = isRecording, onCheckedChange = { isChecked ->
                        // 在这里处理开始/停止录音的逻辑

                        if (isChecked) {
                            // 开始录音
                            speechToTextUtil.startListening()
                        } else {
                            // 停止录音
                            speechToTextUtil.stopListening()
                        }
                        isRecording = isChecked
                    }, modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp)
                        )
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = if (!isRecording) ImageVector.vectorResource(R.drawable.baseline_keyboard_voice_24)
                        else ImageVector.vectorResource(R.drawable.baseline_stop_24),
                        contentDescription = "keyboard voice",
                        tint = Color.White,
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))
        }
        if (isToolbarShow.value) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .focusRequester(focusRequester)
            ) {

                Row {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(Icons.Filled.Check, contentDescription = "Localized description")
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Localized description",
                        )
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            Icons.Filled.Mic,
                            contentDescription = "Localized description",
                        )
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            Icons.Filled.Image,
                            contentDescription = "Localized description",
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun DiaryItem(
    title: String, context: String, pos: String, type: String, imageUrls: List<String>
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
                .requiredHeightIn(max = 250.dp) // 设置最大高度为250dp
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
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = pos,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}