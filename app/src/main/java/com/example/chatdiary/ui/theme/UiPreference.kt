package com.example.chatdiary.ui.theme

import com.example.chatdiary.util.DeviceUtil
import com.example.chatdiary.util.secure.PreferenceStore

class UiPreferences(
    private val preferenceStore: PreferenceStore,
) {
    fun themeMode() = preferenceStore.getString(
        "pref_theme_mode_key",
        ThemeMode.SYSTEM.name,
    )

    fun appTheme() = preferenceStore.getString(
        "pref_app_theme",
        if (DeviceUtil.isDynamicColorAvailable) {
            AppTheme.MONET.name
        } else {
            AppTheme.DEFAULT.name
        },
    )

    fun themeDarkAmoled() = preferenceStore.getBoolean("pref_theme_dark_amoled_key", false)

}
