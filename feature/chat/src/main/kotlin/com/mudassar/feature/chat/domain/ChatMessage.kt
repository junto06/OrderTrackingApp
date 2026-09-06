package com.mudassar.feature.chat.domain

data class ChatMessage(
    val id: MessageId,
    val sender: Sender,
    val text: String,
    val timestamp: Long,
)

@JvmInline
value class MessageId(val value: String)

enum class Sender {
    CUSTOMER,
    DRIVER,
}
