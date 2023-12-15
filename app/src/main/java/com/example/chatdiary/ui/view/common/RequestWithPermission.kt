package com.example.chatdiary.ui.view.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestWithPermission(
    permission: String,
    onError: @Composable () -> Unit,
    onSuccess: @Composable () -> Unit
) {
    val state = rememberPermissionState(permission)
    LaunchedEffect(key1 = true) {
        state.launchPermissionRequest()
    }
    if (state.status.isGranted) {
        onSuccess()
    } else {
        onError()
    }
}
