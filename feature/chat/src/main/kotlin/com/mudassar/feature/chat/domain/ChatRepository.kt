package com.mudassar.feature.chat.domain

import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeMessages(orderId: String): Flow<ChatMessage>
    suspend fun sendMessage(orderId: String, text: String)
}