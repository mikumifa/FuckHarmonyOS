package com.example.chatdiary2.util.secure
import AuthenticatorUtil
import AuthenticatorUtil.startAuthentication
import com.example.chatdiary2.delegate.SecureActivityDelegate
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnlockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startAuthentication(
            "ChatDiary",
            confirmationRequired = false,
            callback = object : AuthenticatorUtil.AuthenticationCallback() {
                override fun onAuthenticationError(
                    activity: FragmentActivity?,
                    errorCode: Int,
                    errString: CharSequence,
                ) {
                    super.onAuthenticationError(activity, errorCode, errString)
                    finishAffinity()
                }

                override fun onAuthenticationSucceeded(
                    activity: FragmentActivity?,
                    result: BiometricPrompt.AuthenticationResult,
                ) {
                    super.onAuthenticationSucceeded(activity, result)
                    SecureActivityDelegate.unlock()
                    finish()
                }
            },
        )
    }
}
