package com.example.chatdiary2.ui.view.main.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdiary2.config.HttpCode
import com.example.chatdiary2.data.UserVO
import com.example.chatdiary2.service.LoginRequest
import com.example.chatdiary2.service.RegisterRequest
import com.example.chatdiary2.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userService: UserService) : ViewModel() {
    fun loginUser(email: String, password: String): LiveData<UserVO?> {
        val loginResult = MutableLiveData<UserVO?>()
        viewModelScope.launch {
            kotlin.runCatching {
                userService.login(LoginRequest(email, password))
            }.onSuccess {
                if (it.httpCode == HttpCode.SUCCESS) loginResult.value = it.data
                else {
                    loginResult.value = null
                }
            }.onFailure {
                loginResult.value = null
            }

        }
        return loginResult
    }

    fun registerUser(
        username: String, password: String, email: String
    ): MutableLiveData<Pair<String, Boolean>> {
        val newUser = RegisterRequest(username = username, password = password, email = email)
        val result = MutableLiveData<Pair<String, Boolean>>()
        viewModelScope.launch {
            kotlin.runCatching {
                userService.register(newUser)

            }.onFailure {
                result.value = Pair("网络问题", false)
            }.onSuccess {
                if (it.httpCode == HttpCode.SUCCESS) {
                    result.value = Pair("注册成功,返回登录界面", true)
                } else {
                    result.value = Pair(it.msg, false)
                }
            }
        }
        return result;
    }
}