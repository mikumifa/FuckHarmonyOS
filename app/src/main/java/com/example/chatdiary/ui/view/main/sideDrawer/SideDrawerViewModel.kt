package com.example.chatdiary.ui.view.main.sideDrawer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdiary.config.HttpCode
import com.example.chatdiary.data.UserVO
import com.example.chatdiary.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SideDrawerViewModel @Inject constructor(private val userService: UserService) : ViewModel() {
    fun getUserInfo(): LiveData<UserVO?> {
        val res = MutableLiveData<UserVO>()
        viewModelScope.launch {
            kotlin.runCatching {
                userService.userInfo()
            }.onSuccess {
                if (it.httpCode == HttpCode.SUCCESS) res.value = it.data
                else res.value = null
            }.onFailure {
                res.value = null
            }
        }
        return res
    }
}