package com.example.chatdiary.ui.view.settings.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdiary.config.HttpCode
import com.example.chatdiary.data.UserVO
import com.example.chatdiary.service.EditUserInfoRequest
import com.example.chatdiary.service.EditUserNameRequest
import com.example.chatdiary.service.EditUserPasswordRequest
import com.example.chatdiary.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
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
            }.onFailure {
                Log.w("sendImage", it.toString())
                result.value = null
            }
        }
        return result

    }

}