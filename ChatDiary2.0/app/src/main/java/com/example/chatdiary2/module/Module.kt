package com.example.chatdiary2.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import androidx.room.TypeConverter
import com.example.chatdiary2.config.BuildConfig
import com.example.chatdiary2.config.Constants
import com.example.chatdiary2.service.DiaryService
import com.example.chatdiary2.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).client(okHttp)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideOkHttpClient(sharedPreferences: SharedPreferences): OkHttpClient {
        val addCookieInterceptor = AddCookieInterceptor(sharedPreferences)
        val receivedCookiesInterceptor = ReceivedCookiesInterceptor(sharedPreferences)

        val builder = OkHttpClient.Builder().addInterceptor(addCookieInterceptor)
            .addInterceptor(receivedCookiesInterceptor)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor())
            builder.addInterceptor(HttpLoggingInterceptor { message ->
                val strLength: Int = message.length
                var start = 0
                var end = 2000
                for (i in 0..99) {
                    if (strLength > end) {
                        Log.d("okhttp", message.substring(start, end))
                        start = end
                        end += 2000
                    } else {
                        Log.d("okhttp", message.substring(start, strLength))
                        break
                    }
                }
            }.setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return builder.build()
    }

    @Provides
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    fun provideDiaryService(retrofit: Retrofit): DiaryService =
        retrofit.create(DiaryService::class.java)
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

class AddCookieInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        // 从应用的存储中获取 Cookie
        val cookie = sharedPreferences.getString("authCookie", "")

        if (cookie != null) {
            if (cookie.isNotEmpty()) {
                builder.addHeader("Cookie", cookie)
            }
        }

        return chain.proceed(builder.build())
    }
}

class ReceivedCookiesInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies = originalResponse.headers("Set-Cookie")
            // 将接收到的 Cookie 保存到应用的存储中
            val editor = sharedPreferences.edit()
            for (cookie in cookies) {
                editor.putString("authCookie", cookie)
            }
            editor.apply()
        }
        return originalResponse
    }
}

