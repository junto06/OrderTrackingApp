package com.mudassar.feature.chat.di

import com.mudassar.feature.chat.data.ChatRepositoryImpl
import com.mudassar.feature.chat.domain.ChatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {
    @Binds
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository
}
