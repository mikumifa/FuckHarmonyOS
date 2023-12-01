package com.example.chatdiary2.ui.view.sideDrawer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdiary2.config.HttpCode
import com.example.chatdiary2.data.UserVO
import com.example.chatdiary2.service.UserService
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