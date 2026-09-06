package com.mudassar.core.rpc.dto

data class ChatMessageDto(
    val id: String,
    val orderId: String,
    val sender: ChatSenderDto,
    val text: String,
    val timestamp: Long,
)

enum class ChatSenderDto {
    CUSTOMER,
    DRIVER,
}
