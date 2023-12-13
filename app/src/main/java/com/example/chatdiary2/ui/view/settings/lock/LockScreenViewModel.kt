package com.example.chatdiary2.ui.view.settings.lock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdiary2.util.secure.SecurityPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockScreenViewModel @Inject constructor(private val securityPreferences: SecurityPreferences) :
    ViewModel() {
    private var _useAuthenticator = securityPreferences.useAuthenticator().stateIn(viewModelScope)
    public val useAuthenticator get() = _useAuthenticator

    public fun setuseAuthenticator(value: Boolean) {
        viewModelScope.launch {
            securityPreferences.useAuthenticator().set(value)
        }
    }
}