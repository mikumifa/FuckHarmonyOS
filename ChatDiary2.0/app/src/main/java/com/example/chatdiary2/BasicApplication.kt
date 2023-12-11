package com.example.chatdiary2

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.chatdiary2.delegate.SecureActivityDelegate
import com.example.chatdiary2.util.secure.SecurityPreferences
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BasicApplication : Application()