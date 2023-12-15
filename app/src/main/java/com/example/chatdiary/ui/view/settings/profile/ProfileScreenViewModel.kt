package com.example.chatdiary.ui.view.settings.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdiary.config.HttpCode
import com.example.chatdiary.data.UiResult
import com.example.chatdiary.data.UserVO
import com.example.chatdiary.service.EditUserInfoRequest
import com.example.chatdiary.service.EditUserNameRequest
import com.example.chatdiary.service.EditUserPasswordRequest
import com.example.chatdiary.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(private val userService: UserService) :
    ViewModel() {
    private val _userInfo = MutableStateFlow(UiResult<UserVO?>(null))
    val userInfo get() = _userInfo.asStateFlow()

    init {
        getUserInfoFlow()
    }

    private fun getUserInfoFlow() {
        viewModelScope.launch(Dispatchers.IO) {
            _userInfo.update {
                it.copy(
                    isOK = false, msg = "加载中"
                )
            }
            val res = userService.userInfo()
            if (res.httpCode == HttpCode.SUCCESS) {
                _userInfo.update {
                    it.copy(
                        data = res.data, isOK = true, msg = "成功", isSuccess = true
                    )
                }
            } else {
                _userInfo.update {
                    it.copy(
                        isOK = true, msg = "失败", isSuccess = false
                    )
                }
            }
        }
    }

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


    fun uploadImage(
        uri: String
    ): MutableLiveData<String> {
        val result = MutableLiveData<String>()
        viewModelScope.launch {
            kotlin.runCatching {
                val file = File(uri)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val urlImage = MultipartBody.Part.createFormData("image", file.name, requestFile)
                userService.uploadImage(
                    image = urlImage
                )
            }.onSuccess {
                Log.w("sendImage", it.toString())
                result.value = it.data
                getUserInfoFlow()
            }.onFailure {
                Log.w("sendImage", it.toString())
                result.value = null
            }
        }
        return result

    }

}