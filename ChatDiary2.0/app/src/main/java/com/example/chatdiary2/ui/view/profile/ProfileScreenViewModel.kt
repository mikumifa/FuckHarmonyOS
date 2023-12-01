package com.example.chatdiary2.ui.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdiary2.config.HttpCode
import com.example.chatdiary2.data.UserVO
import com.example.chatdiary2.service.EditUserInfoRequest
import com.example.chatdiary2.service.EditUserNameRequest
import com.example.chatdiary2.service.EditUserPasswordRequest
import com.example.chatdiary2.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(private val userService: UserService) :
    ViewModel() {
    fun editUserName(username: String): LiveData<Pair<String, Boolean>> {
        val res = MutableLiveData<Pair<String, Boolean>>()
        viewModelScope.launch {
            kotlin.runCatching {
                userService.editName(EditUserNameRequest(username))
            }.onSuccess {
                if (it.httpCode == HttpCode.SUCCESS) {
                    res.value = Pair("修改成功", true)
                } else {
                    res.value = Pair(it.msg, false)
                }
            }.onFailure {
                res.value = Pair("可能网络有问题", false)

            }

        }
        return res
    }

    fun editUserInfo(info: String): LiveData<Pair<String, Boolean>> {
        val res = MutableLiveData<Pair<String, Boolean>>()
        viewModelScope.launch {
            kotlin.runCatching {
                userService.editInfo(EditUserInfoRequest(info))
            }.onSuccess {
                if (it.httpCode == HttpCode.SUCCESS) {
                    res.value = Pair("修改成功", true)
                } else {
                    res.value = Pair(it.msg, false)
                }
                res.value = Pair("可能网络有问题", it.httpCode == HttpCode.SUCCESS)
            }.onFailure {
                res.value = Pair("可能网络有问题", false)

            }

        }
        return res
    }

    fun editUserPassword(password: String): LiveData<Pair<String, Boolean>> {
        val res = MutableLiveData<Pair<String, Boolean>>()
        viewModelScope.launch {
            kotlin.runCatching {
                userService.editPassword(EditUserPasswordRequest(password))
            }.onSuccess {

                if (it.httpCode == HttpCode.SUCCESS) {
                    res.value = Pair("修改成功", true)
                } else {
                    res.value = Pair(it.msg, false)
                }
                res.value = Pair("请求成功", it.httpCode == HttpCode.SUCCESS)
            }.onFailure {
                res.value = Pair("可能网络有问题", false)

            }

        }
        return res
    }

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