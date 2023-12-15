package com.example.chatdiary.util

import com.google.android.material.color.DynamicColors

object DeviceUtil {
    val isDynamicColorAvailable by lazy {
        DynamicColors.isDynamicColorAvailable()
    }


}