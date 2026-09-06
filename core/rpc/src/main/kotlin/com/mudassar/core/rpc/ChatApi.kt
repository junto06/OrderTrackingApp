package com.mudassar.core.rpc

import com.mudassar.core.rpc.dto.ChatMessageDto
import kotlinx.coroutines.flow.Flow

interface ChatApi {
    fun observeMessages(orderId: String): Flow<ChatMessageDto>
    suspend fun sendMessage(orderId: String, text: String)
}