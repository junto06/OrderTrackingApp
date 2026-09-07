package com.mudassar.feature.chat.data.persistence

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages WHERE orderId = :orderId ORDER BY timestamp ASC")
    fun getMessagesForOrder(orderId: String): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages WHERE syncStatus = 'PENDING'")
    suspend fun getPendingMessages(): List<ChatMessageEntity>

    @Upsert
    suspend fun upsertMessage(vararg messages: ChatMessageEntity)
}
