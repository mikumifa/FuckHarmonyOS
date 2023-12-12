package com.example.chatdiary2.ui.view.settings.appearance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdiary2.ui.theme.AppTheme
import com.example.chatdiary2.ui.theme.UiPreferences
import com.example.chatdiary2.util.secure.PreferenceStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel

class AppearanceViewModel @Inject constructor(private val preferenceStore: PreferenceStore) :
    ViewModel() {
    val uiPreferences = UiPreferences(preferenceStore)
    var appThemeStateFlow = uiPreferences.appTheme().stateIn(viewModelScope)

}