import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncryptionUtils(private val context: Context) {

    private val masterKeyAlias =
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "user_prefs",
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun encryptAndSave(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun decrypt(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }
}

object UserPref {
    fun savePassword(userId: Long, username: String, password: String, context: Context) {

        val encryptionUtils = EncryptionUtils(context)
        encryptionUtils.encryptAndSave("userId", userId.toString())
        encryptionUtils.encryptAndSave("password", password)
        encryptionUtils.encryptAndSave("email", username)
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", true)
            apply()
        }
    }
}
