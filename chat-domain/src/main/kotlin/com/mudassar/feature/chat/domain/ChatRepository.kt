package com.mudassar.feature.chat.domain

import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeMessages(orderId: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(orderId: String, text: String)
    suspend fun syncPendingMessages(): Boolean
}