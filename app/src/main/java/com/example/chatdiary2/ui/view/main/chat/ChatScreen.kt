package com.example.chatdiary2.ui.view.main.chat

import SpeechToTextUtil
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.IndeterminateCheckBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import com.example.chatdiary2.R
import com.example.chatdiary2.service.Message
import com.example.chatdiary2.ui.view.common.keyboardAsState
import com.example.chatdiary2.ui.view.main.diary.InputSelector
import com.example.chatdiary2.ui.view.main.diary.SelectorExpanded
import com.example.chatdiary2.ui.view.main.diary.TimedDialog
import com.example.chatdiary2.ui.view.nav.Action

@Composable
private fun LoadingComponent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator() // 显示一个进度条
    }
}

@Composable
fun ChatScreen(
    action: Action,
    chatViewModel: ChatViewModel = hiltViewModel(),
) {
    ConversationContent(action = action, uiState = chatViewModel);
}

private val ChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)

@Composable
fun ChatItemBubble(
    message: Message,
    isUserMe: Boolean,
) {

    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Column {
        Surface(
            color = backgroundBubbleColor, shape = ChatBubbleShape
        ) {
            ClickableMessage(
                message = message,
                isUserMe = isUserMe,
            )
        }
    }
}

@Composable
fun ClickableMessage(
    message: Message,
    isUserMe: Boolean,
) {
    val uriHandler = LocalUriHandler.current

    val styledMessage = messageFormatter(
        text = message.content, primary = isUserMe
    )

    ClickableText(text = styledMessage,
        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(16.dp),
        onClick = {
            styledMessage.getStringAnnotations(start = it, end = it).firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
                        else -> Unit
                    }
                }
        })
}

@Composable
fun Messages(
    messages: List<Message>, scrollState: LazyListState, modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LazyColumn(
            reverseLayout = true, state = scrollState, modifier = Modifier.fillMaxSize()
        ) {
            for (index in messages.indices) {
                val prevAuthor = messages.getOrNull(index - 1)?.isUserMe
                val nextAuthor = messages.getOrNull(index + 1)?.isUserMe
                val thisMsg = messages[index]
                val isFirstMessageByAuthor = prevAuthor != thisMsg.isUserMe
                val isLastMessageByAuthor = nextAuthor != thisMsg.isUserMe

                item {
                    Message(
                        msg = thisMsg,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
                        isLastMessageByAuthor = isLastMessageByAuthor
                    )
                }
            }
        }
    }
}


@Composable
fun Message(
    msg: Message, isFirstMessageByAuthor: Boolean, isLastMessageByAuthor: Boolean
) {
    val borderColor = if (msg.isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }

    val spaceBetweenAuthors = if (isLastMessageByAuthor) Modifier.padding(top = 8.dp) else Modifier
    Row(modifier = spaceBetweenAuthors) {

        if (isLastMessageByAuthor) {
            Image(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Top),
                painter = if (msg.isUserMe) painterResource(R.drawable.temp_user) else painterResource(
                    R.drawable.chatgpt
                ),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        } else {
            // Space under avatar
            Spacer(modifier = Modifier.width(74.dp))
        }
        AuthorAndTextMessage(
            msg = msg,
            isUserMe = msg.isUserMe,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isLastMessageByAuthor,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        )
    }
}

@Composable
private fun AuthorNameTimestamp(msg: Message) {
    Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
        Text(
            text = if (msg.isUserMe) "Me" else "GPT",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp) // Space to 1st bubble
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = msg.timestamp,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignBy(LastBaseline),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AuthorAndTextMessage(
    msg: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (isLastMessageByAuthor) {
            AuthorNameTimestamp(msg)
        }
        ChatItemBubble(msg, isUserMe)
        if (isFirstMessageByAuthor) {
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            // Between bubbles
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ConversationContent(
    action: Action, modifier: Modifier = Modifier, uiState: ChatViewModel = hiltViewModel()

) {


    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    var messageList by remember { mutableStateOf(emptyList<Message>()) }

    Column(
        Modifier.fillMaxSize()
    ) {

        val messages = uiState.getMessageFlow()
        messages.observe(lifecycleOwner) {
            messageList = it
        }
        Messages(
            messages = messageList, modifier = Modifier.weight(1f), scrollState = scrollState
        )
        val sendingMessage: (String) -> MutableLiveData<Boolean> = { content ->
            val res = uiState.addChat(
                content
            )
            val result = MutableLiveData<Boolean>()
            res.observe(lifecycleOwner) {
                result.value = it
            }
            result
        }
        UserInput(
            sendingMessage = sendingMessage
        ) {
            val messages = uiState.getMessageFlow()
            messages.observe(lifecycleOwner) {
                messageList = it
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInput(
    sendingMessage: (String) -> MutableLiveData<Boolean>,
    onSent: () -> Unit,
) {
    var text by remember { mutableStateOf(TextFieldValue()) }
    //   val text = rememberSaveable { mutableStateOf("") }
    val isSending = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    val isErrorShow = remember { mutableStateOf(false) }
    var errorShowInfo by remember { mutableStateOf("") }
    val speechToTextUtil = SpeechToTextUtil(LocalContext.current)
    val isToolbarShow = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val isKeyboardOpen by keyboardAsState() // true or false
    var currentInputSelector by rememberSaveable { mutableStateOf(InputSelector.NONE) }
    speechToTextUtil.setSpeechRecognitionListener(onSpeechRecognitionResult = {
        text = TextFieldValue(it)
    }, onSpeechRecognitionError = {
        isErrorShow.value = true
        errorShowInfo = "语言识别失败"
    })
    TimedDialog(showDialog = isErrorShow,
        durationMillis = 1000,
        text = errorShowInfo,
        onDismiss = {})

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)) {
        if (isToolbarShow.value) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            ) {

                Row {
                    IconButton(onClick = { currentInputSelector = InputSelector.EMOJI }) {
                        Icon(
                            Icons.Filled.EmojiEmotions, contentDescription = "Localized description",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                    }
                }
            }

        }
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
                        tint = MaterialTheme.colorScheme.onSecondaryContainer

                    )
                }
            }
            OutlinedTextField(value = text, onValueChange = {
                text = it
                isSending.value = it.text.isNotBlank()
            },
                singleLine = false, modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        currentInputSelector = InputSelector.NONE
                    }
                    .padding(4.dp),

                shape = RoundedCornerShape(18.dp), colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ))
            if (isLoading.value) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp)
                        )
                        .clip(CircleShape), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp), // 调整圆环的大小
                        color = Color.White
                    )
                }
            } else {
                if (isSending.value) {
                    IconButton(
                        onClick = {
                            isLoading.value = true
                            val isSuccess = sendingMessage(text.text)
                            isSuccess.observe(lifecycleOwner) {
                                if (it) {
                                    onSent()
                                    isLoading.value = false
                                    text = TextFieldValue()
                                } else {
                                    isLoading.value = false
                                    isErrorShow.value = true
                                    errorShowInfo = "发送错误"
                                }
                                isSending.value = text.text.isNotBlank()

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
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                } else {
                    IconToggleButton(
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
                                MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(12.dp)
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
        SelectorExpanded(
            onTextAdded = {
                val currentText = text.text
                val cursorPosition = text.selection.end
                val newText = currentText.substring(0, cursorPosition) + it + currentText.substring(
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

@Composable
fun messageFormatter(
    text: String, primary: Boolean
): AnnotatedString {
    val tokens = symbolPattern.findAll(text)
    return buildAnnotatedString {
        var cursorPosition = 0
        val codeSnippetBackground = if (primary) {
            MaterialTheme.colorScheme.secondary
        } else {
            MaterialTheme.colorScheme.surface
        }
        for (token in tokens) {
            append(text.slice(cursorPosition until token.range.first))
            val (annotatedString, stringAnnotation) = getSymbolAnnotation(
                matchResult = token,
                colorScheme = MaterialTheme.colorScheme,
                primary = primary,
                codeSnippetBackground = codeSnippetBackground
            )
            append(annotatedString)
            if (stringAnnotation != null) {
                val (item, start, end, tag) = stringAnnotation
                addStringAnnotation(tag = tag, start = start, end = end, annotation = item)
            }
            cursorPosition = token.range.last + 1
        }
        if (!tokens.none()) {
            append(text.slice(cursorPosition..text.lastIndex))
        } else {
            append(text)
        }
    }
}

// Regex containing the syntax tokens
val symbolPattern by lazy {
    Regex("""(https?://[^\s\t\n]+)|(`[^`]+`)|(@\w+)|(\*[\w]+\*)|(_[\w]+_)|(~[\w]+~)""")
}

enum class SymbolAnnotationType {
    LINK
}
typealias StringAnnotation = AnnotatedString.Range<String>
typealias SymbolAnnotation = Pair<AnnotatedString, StringAnnotation?>


private fun getSymbolAnnotation(
    matchResult: MatchResult,
    colorScheme: ColorScheme,
    primary: Boolean,
    codeSnippetBackground: Color
): SymbolAnnotation {
    return when (matchResult.value.first()) {
        '*' -> SymbolAnnotation(
            AnnotatedString(
                text = matchResult.value.trim('*'),
                spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
            ), null
        )

        '_' -> SymbolAnnotation(
            AnnotatedString(
                text = matchResult.value.trim('_'),
                spanStyle = SpanStyle(fontStyle = FontStyle.Italic)
            ), null
        )

        '~' -> SymbolAnnotation(
            AnnotatedString(
                text = matchResult.value.trim('~'),
                spanStyle = SpanStyle(textDecoration = TextDecoration.LineThrough)
            ), null
        )

        '`' -> SymbolAnnotation(
            AnnotatedString(
                text = matchResult.value.trim('`'), spanStyle = SpanStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    background = codeSnippetBackground,
                    baselineShift = BaselineShift(0.2f)
                )
            ), null
        )

        'h' -> SymbolAnnotation(
            AnnotatedString(
                text = matchResult.value, spanStyle = SpanStyle(
                    color = if (primary) colorScheme.inversePrimary else colorScheme.primary
                )
            ), StringAnnotation(
                item = matchResult.value,
                start = matchResult.range.first,
                end = matchResult.range.last,
                tag = SymbolAnnotationType.LINK.name
            )
        )

        else -> SymbolAnnotation(AnnotatedString(matchResult.value), null)
    }
}

private val JumpToBottomThreshold = 56.dp