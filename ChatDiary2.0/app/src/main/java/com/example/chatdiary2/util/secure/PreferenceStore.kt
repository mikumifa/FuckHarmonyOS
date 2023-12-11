package com.example.chatdiary2.util.secure

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 *  改写自tachiyomi源码
 */

//可以用下面的来读取，
class PreferenceStore(
    private val sharedPreferences: SharedPreferences,
) {

    private val keyFlow = sharedPreferences.keyFlow

    fun getString(key: String, defaultValue: String): Preference<String> {
        return AndroidPreference.StringPrimitive(sharedPreferences, keyFlow, key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long): Preference<Long> {
        return AndroidPreference.LongPrimitive(sharedPreferences, keyFlow, key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int): Preference<Int> {
        return AndroidPreference.IntPrimitive(sharedPreferences, keyFlow, key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float): Preference<Float> {
        return AndroidPreference.FloatPrimitive(sharedPreferences, keyFlow, key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Preference<Boolean> {
        return AndroidPreference.BooleanPrimitive(sharedPreferences, keyFlow, key, defaultValue)
    }

    fun getStringSet(key: String, defaultValue: Set<String>): Preference<Set<String>> {
        return AndroidPreference.StringSetPrimitive(sharedPreferences, keyFlow, key, defaultValue)
    }

    fun getAll(): Map<String, *> {
        return sharedPreferences.all ?: emptyMap<String, Any>()
    }
}


//扩展了SharePreference的keyFlow
// [callbackFlow] 创造一个冷流，
private val SharedPreferences.keyFlow
    get() = callbackFlow {
        //Called when a shared preference is changed, added, or removed. This may be called even if a preference is set to its existing value.
        //接口函数， java里面可以直接赋值函数，Kotlin直接接大括号就能重写
        //重写内容， 把变化的可以send出去
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key: String? ->
            trySend(
                key,
            )
        }

        //注册
        registerOnSharedPreferenceChangeListener(listener)

        //流被关闭时候注销注册
        awaitClose {
            unregisterOnSharedPreferenceChangeListener(listener)
        }
    }