package com.mudassar.feature.chat.di

import android.content.Context
import androidx.room.Room
import com.mudassar.feature.chat.data.persistence.ChatDao
import com.mudassar.feature.chat.data.persistence.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatDataModule {
    @Provides
    @Singleton
    fun provideChatDatabase(@ApplicationContext context: Context): ChatDatabase =
        Room.databaseBuilder(context, ChatDatabase::class.java, "chat.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideChatDao(database: ChatDatabase): ChatDao = database.chatDao()
}
