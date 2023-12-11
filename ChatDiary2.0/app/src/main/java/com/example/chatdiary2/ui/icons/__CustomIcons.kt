package com.example.chatdiary2.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.chatdiary2.ui.icons.customicons.Github
import kotlin.collections.List as ____KtList

public object CustomIcons

private var __AllIcons: ____KtList<ImageVector>? = null

public val CustomIcons.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf(Github)
    return __AllIcons!!
  }
