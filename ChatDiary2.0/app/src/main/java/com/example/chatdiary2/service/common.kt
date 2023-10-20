package com.example.chatdiary2.service

data class CommonResponse<T>(
    val code: Int, val msg: String, val data: T?, val httpCode: Int
) {
    companion object {
        fun <T> success(data: T?): CommonResponse<T> {
            return success(data, 200)
        }

        fun <T> success(httpCode: Int): CommonResponse<T> {
            return success(null, httpCode)
        }

        fun <T> success(data: T?, httpCode: Int): CommonResponse<T> {
            return CommonResponse(0, "success", data, httpCode)
        }

        fun <T> error(type: BizError): CommonResponse<T> {
            return CommonResponse(type.code, type.message, null, type.httpCode)
        }

        fun <T> error(type: BizError, msg: String): CommonResponse<T> {
            return CommonResponse(type.code, msg, null, type.httpCode)
        }
    }
}

enum class BizError(val code: Int, val message: String, val httpCode: Int) {
    USERNAME_EXISTS(200001, "用户名已存在", 400), INVALID_CREDENTIAL(
        200002,
        "用户名或密码错误",
        400
    ),
    USER_NOT_FOUND(200003, "用户不存在", 400)
}
