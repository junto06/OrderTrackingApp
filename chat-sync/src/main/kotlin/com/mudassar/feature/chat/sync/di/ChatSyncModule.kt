package com.mudassar.feature.chat.sync.di

import com.mudassar.feature.chat.domain.ChatSyncScheduler
import com.mudassar.feature.chat.sync.WorkManagerChatSyncScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatSyncModule {
    @Binds
    abstract fun bindChatSyncScheduler(impl: WorkManagerChatSyncScheduler): ChatSyncScheduler
}