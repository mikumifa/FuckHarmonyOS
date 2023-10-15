package com.example.chatdiary2.di

import com.example.chatdiary2.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UserModule {

    @Provides
    fun provideUserService(): UserService = UserService.create()
}