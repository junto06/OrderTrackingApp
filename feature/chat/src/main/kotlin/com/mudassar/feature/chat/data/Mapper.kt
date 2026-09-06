package com.mudassar.feature.chat.data

import com.mudassar.core.rpc.dto.ChatMessageDto
import com.mudassar.core.rpc.dto.ChatSenderDto
import com.mudassar.feature.chat.domain.ChatMessage
import com.mudassar.feature.chat.domain.MessageId
import com.mudassar.feature.chat.domain.Sender

internal fun ChatMessageDto.toDomain(): ChatMessage =
    ChatMessage(
        id = MessageId(id),
        sender = sender.toDomain(),
        text = text,
        timestamp = timestamp,
    )

private fun ChatSenderDto.toDomain(): Sender = when (this) {
    ChatSenderDto.CUSTOMER -> Sender.CUSTOMER
    ChatSenderDto.DRIVER -> Sender.DRIVER
}
