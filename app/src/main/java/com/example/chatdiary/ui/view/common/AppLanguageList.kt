package com.example.chatdiary.ui.view.common

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.example.chatdiary.R
import org.xmlpull.v1.XmlPullParser
import java.util.Locale


@OptIn(ExperimentalStdlibApi::class)
@Composable
fun AppLanguageList(
) {
    val context = LocalContext.current
    val langs = remember { getLangs(context) }
    val langsList: List<Map.Entry<String, String>> = langs.entries.toList()
    var currentLanguage by remember {
        mutableStateOf(AppCompatDelegate.getApplicationLocales().get(0)?.toLanguageTag() ?: "")
    }
    LaunchedEffect(currentLanguage) {
        val locale = if (currentLanguage.isEmpty()) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(currentLanguage)
        }
        AppCompatDelegate.setApplicationLocales(locale)
    }
    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            count = langsList.size
        ) { idx ->
            val lang = langsList[idx]
            Column(
                modifier = Modifier
                    .width(160.dp)
                    .padding(top = 8.dp),
            ) {

                AppLanguagePreviewItem(
                    lang.key,
                    lang.value,
                    selected = currentLanguage == lang.key,
                    onClick = { newValue ->
                        currentLanguage = newValue
                    },
                )

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

private fun getDisplayName(lang: String?): String {
    if (lang == null) {
        return ""
    }
    val locale = when (lang) {
        "" -> LocaleListCompat.getAdjustedDefault()[0]
        "en-rGB" -> Locale.forLanguageTag("en")

        else -> Locale.forLanguageTag(lang)
    }
    return locale!!.getDisplayName(locale).replaceFirstChar { it.uppercase(locale) }
}

private fun getLangs(context: Context): Map<String, String> {
    val langs = mutableListOf<Pair<String, String>>()
    val parser = context.resources.getXml(R.xml.locales_config)
    var eventType = parser.eventType
    while (eventType != XmlPullParser.END_DOCUMENT) {
        if (eventType == XmlPullParser.START_TAG && parser.name == "locale") {
            for (i in 0 until parser.attributeCount) {
                if (parser.getAttributeName(i) == "name") {
                    val langTag = parser.getAttributeValue(i)
                    val displayName = getDisplayName(langTag)
                    if (displayName.isNotEmpty()) {
                        langs.add(Pair(langTag, displayName))
                    }
                }
            }
        }
        eventType = parser.next()
    }

    langs.sortBy { it.second }
    langs.add(0, Pair("",context.getString(R.string.system_lang)))
    return langs.toMap()
}

@SuppressLint("ComposeModifierMissing")
@Composable
fun AppLanguagePreviewItem(
    lang_tag: String,
    lang_display: String,
    selected: Boolean,
    onClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.1f)
            .border(
                width = 4.dp,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    DividerDefaults.color
                },
                shape = RoundedCornerShape(17.dp),
            )
            .padding(4.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = { onClick(lang_tag) }),
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.weight(0.3f),
                contentAlignment = Alignment.CenterEnd,
            ) {
                if (selected) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "选择",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
        Text(
            text = lang_display,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            maxLines = 2,
            style = MaterialTheme.typography.titleLarge,
        )

    }
}
