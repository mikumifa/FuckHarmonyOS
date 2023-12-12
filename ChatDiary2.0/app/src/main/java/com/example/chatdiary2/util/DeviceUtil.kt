package com.example.chatdiary2.util

import android.os.Build
import com.google.android.material.color.DynamicColors

object DeviceUtil {
    val isDynamicColorAvailable by lazy {
        DynamicColors.isDynamicColorAvailable()
    }


}