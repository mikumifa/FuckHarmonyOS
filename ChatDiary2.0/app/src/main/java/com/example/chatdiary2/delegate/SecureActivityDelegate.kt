package com.example.chatdiary2.delegate

import AuthenticatorUtil
import com.example.chatdiary2.util.secure.SecurityPreferences

class SecureActivityDelegate() {
    companion object {
        var requireUnlock = true
        fun unlock() {
            requireUnlock = false
        }

        fun onApplicationStopped(preferences: SecurityPreferences) {
            if (!preferences.useAuthenticator().get()) return
            if (!AuthenticatorUtil.isAuthenticating) {
                // Return if app is closed in locked state
                if (requireUnlock) return
                // Save app close time if lock is delayed
                if (preferences.lockAppAfter().get() > 0) {
                    preferences.lastAppClosed().set(System.currentTimeMillis())
                }
            }
        }

        /**
         * Checks if unlock is needed when app comes foreground.
         */
        fun onApplicationStart(preferences: SecurityPreferences) {
            if (!preferences.useAuthenticator().get()) return
            val lastClosedPref = preferences.lastAppClosed()

            // `requireUnlock` can be true on process start or if app was closed in locked state
            if (!AuthenticatorUtil.isAuthenticating && !requireUnlock) {
                requireUnlock = when (val lockDelay = preferences.lockAppAfter().get()) {
                    -1 -> false // Never
                    0 -> true // Always
                    else -> lastClosedPref.get() + lockDelay * 60_000 <= System.currentTimeMillis()
                }
            }
            lastClosedPref.delete()
        }
    }

}
