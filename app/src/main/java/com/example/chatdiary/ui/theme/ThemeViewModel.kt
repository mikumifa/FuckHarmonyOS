package com.example.chatdiary.ui.theme;

import androidx.lifecycle.ViewModel
import com.example.chatdiary.util.secure.PreferenceStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ThemeViewModel @Inject constructor(private val preferenceStore: PreferenceStore) :
    ViewModel() {
    val uiPreferences = UiPreferences(preferenceStore)
}
