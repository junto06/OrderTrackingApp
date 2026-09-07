package com.mudassar.feature.chat.di

import com.mudassar.feature.chat.data.ChatRepositoryImpl
import com.mudassar.feature.chat.data.RpcChatApi
import com.mudassar.feature.chat.domain.ChatRepository
import com.mudassar.feature.chat.presenation.ChatTrackerImpl
import com.mudassar.feature.chatapi.ChatInitializer
import com.mudassar.feature.chatapi.ChatTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ChatModule {
    @Binds
    fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    fun bindChatTracker(impl: ChatTrackerImpl): ChatTracker

    @Binds
    fun bindChatInitializer(impl: RpcChatApi): ChatInitializer
}
