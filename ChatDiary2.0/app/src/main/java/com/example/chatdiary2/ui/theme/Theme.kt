package com.example.chatdiary2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import eu.kanade.presentation.theme.colorscheme.GreenAppleColorScheme
import eu.kanade.presentation.theme.colorscheme.LavenderColorScheme
import eu.kanade.presentation.theme.colorscheme.MidnightDuskColorScheme
import eu.kanade.presentation.theme.colorscheme.MonetColorScheme
import eu.kanade.presentation.theme.colorscheme.StrawberryColorScheme
import eu.kanade.presentation.theme.colorscheme.TachiyomiColorScheme
import eu.kanade.presentation.theme.colorscheme.TakoColorScheme
import eu.kanade.presentation.theme.colorscheme.TealTurqoiseColorScheme
import eu.kanade.presentation.theme.colorscheme.TidalWaveColorScheme
import eu.kanade.presentation.theme.colorscheme.YinYangColorScheme
import eu.kanade.presentation.theme.colorscheme.YotsubaColorScheme


@Composable
fun ChatDiaryTheme(
    appTheme: AppTheme? = null,
    amoled: Boolean? = null,
    themeViewModel: ThemeViewModel = hiltViewModel(),
    content: @Composable () -> Unit,

    ) {


    MaterialTheme(
        colorScheme = getThemeColorScheme(
            appTheme ?: AppTheme.valueOf(themeViewModel.uiPreferences.appTheme().get()),
            amoled ?: themeViewModel.uiPreferences.themeDarkAmoled().get(),
            themeViewModel.uiPreferences
        ),
        content = content,
    )
}

@Composable
@ReadOnlyComposable
private fun getThemeColorScheme(
    appTheme: AppTheme?, amoled: Boolean?, uiPreferences: UiPreferences
): ColorScheme {
    val colorScheme = when (appTheme ?: uiPreferences.appTheme().get()) {
        AppTheme.DEFAULT -> TachiyomiColorScheme
        AppTheme.MONET -> MonetColorScheme(LocalContext.current)
        AppTheme.GREEN_APPLE -> GreenAppleColorScheme
        AppTheme.LAVENDER -> LavenderColorScheme
        AppTheme.MIDNIGHT_DUSK -> MidnightDuskColorScheme
        AppTheme.STRAWBERRY_DAIQUIRI -> StrawberryColorScheme
        AppTheme.TAKO -> TakoColorScheme
        AppTheme.TEALTURQUOISE -> TealTurqoiseColorScheme
        AppTheme.TIDAL_WAVE -> TidalWaveColorScheme
        AppTheme.YINYANG -> YinYangColorScheme
        AppTheme.YOTSUBA -> YotsubaColorScheme
        else -> TachiyomiColorScheme
    }
    return colorScheme.getColorScheme(
        isSystemInDarkTheme(),
        amoled ?: uiPreferences.themeDarkAmoled().get(),
    )
}
