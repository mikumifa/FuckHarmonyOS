package com.example.chatdiary2.util.secure

/**
 *  改写自tachiyomi源码
 */
class SecurityPreferences(
    private val preferenceStore: PreferenceStore,
) {

    fun useAuthenticator() = preferenceStore.getBoolean("use_biometric_lock", false)

    fun lockAppAfter() = preferenceStore.getInt("lock_app_after", 0)

    /**
     * For app lock. Will be set when there is a pending timed lock.
     * Otherwise this pref should be deleted.
     */
    fun lastAppClosed() = preferenceStore.getLong(
        Preference.appStateKey("last_app_closed"),
        0,
    )

    object SecureScreenMode {
        const val ALWAYS = "ALWAYS"
        const val INCOGNITO = "INCOGNITO"
        const val NEVER = "NEVER"
    }
}
