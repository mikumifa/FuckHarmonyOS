package com.example.chatdiary2.ui.view.main.table

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdiary2.config.HttpCode
import com.example.chatdiary2.data.HappyValue
import com.example.chatdiary2.data.UiResult
import com.example.chatdiary2.service.HappyValueService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HappyValueViewModel @Inject constructor(private val happyValueService: HappyValueService) :
    ViewModel() {
    private val _happyValueListState = MutableStateFlow(UiResult<List<HappyValue>>(emptyList()))
    val happyValueListState get() = _happyValueListState.asStateFlow()

    init {
        getHappyValueList()
    }

    private fun getHappyValueList() {

        viewModelScope.launch(Dispatchers.IO) {
            _happyValueListState.update {
                it.copy(
                    data = emptyList(), isOK = false, msg = "加载中"
                )
            }
            val res = happyValueService.getHappyValueList(timestamp = Date().time)
            if (res.httpCode == HttpCode.SUCCESS) {
                _happyValueListState.update {
                    it.copy(
                        data = res.data!!, isOK = true, msg = "成功", isSuccess = true
                    )
                }
            } else {
                _happyValueListState.update {
                    it.copy(
                        data = emptyList(), isOK = true, msg = "失败", isSuccess = false
                    )
                }
            }
        }
    }

}