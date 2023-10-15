import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @field:SerializedName("username") val username: String,
    @field:SerializedName("password") val password: String,
    @field:SerializedName("email") val email: String
)

data class LoginRequest(
    @field:SerializedName("username") val username: String,
    @field:SerializedName("password") val password: String
)

data class UserVO(
    val password: String,
    val email: String
)

data class EditUserInfoRequest(
    @field:SerializedName("username") val username: String
)


data class CommonResponse<T>(
    val code: Int,       // 自定义状态码
    val msg: String,     // 状态消息
    val data: T? = null  // 响应数据
)

// 扩展函数，用于检查响应是否成功
fun <T> CommonResponse<T>.isSuccess(): Boolean {
    return code == 0
}

// 扩展函数，用于获取成功的消息
fun <T> CommonResponse<T>.getSuccessMessage(): String? {
    return if (isSuccess()) msg else null
}

// 扩展函数，用于获取错误消息
fun <T> CommonResponse<T>.getErrorMessage(): String? {
    return if (!isSuccess()) msg else null
}
