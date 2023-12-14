package com.example.chatdiary2.util.secure

import AuthenticatorUtil
import AuthenticatorUtil.startAuthentication
import android.app.Activity
import android.content.Intent
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
                    val resultCode = Activity.RESULT_OK
                    val resultIntent = Intent()
                    setResult(resultCode, resultIntent)
                    finishAffinity()
                }

                override fun onAuthenticationSucceeded(
                    activity: FragmentActivity?,
                    result: BiometricPrompt.AuthenticationResult,
                ) {
                    super.onAuthenticationSucceeded(activity, result)
                    SecureActivityDelegate.unlock()
                    val resultCode = Activity.RESULT_OK
                    val resultIntent = Intent()
                    setResult(resultCode, resultIntent)
                    finish()
                }
            },
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        val resultCode = Activity.RESULT_OK
        val resultIntent = Intent()
        setResult(resultCode, resultIntent)
        finish() // 关闭UnlockActivity
    }
}
