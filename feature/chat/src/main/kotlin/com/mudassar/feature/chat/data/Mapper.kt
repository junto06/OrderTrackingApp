package com.mudassar.feature.chat.data

import com.mudassar.feature.chat.data.persistence.ChatMessageEntity
import com.mudassar.feature.chat.data.persistence.SyncStatus
import com.mudassar.feature.chat.domain.ChatMessage
import com.mudassar.feature.chat.domain.MessageId
import com.mudassar.feature.chat.domain.Sender
import com.mudassar.ordertracking.Chat

internal fun Chat.ChatMessage.toEntity(status: SyncStatus = SyncStatus.SYNCED): ChatMessageEntity =
    ChatMessageEntity(
        id = this.id,
        orderId = this.orderId,
        sender = this.sender.name,
        text = this.text,
        timestamp = this.timestamp,
        syncStatus = status
    )

internal fun Chat.ChatMessage.toDomain(): ChatMessage =
    ChatMessage(
        id = MessageId(id),
        sender = sender.toDomain(),
        text = text,
        timestamp = timestamp,
    )

internal fun ChatMessageEntity.toDomain(): ChatMessage =
    ChatMessage(
        id = MessageId(id),
        sender = Sender.valueOf(sender),
        text = text,
        timestamp = timestamp,
    )

private fun Chat.Sender.toDomain(): Sender = when (this) {
    Chat.Sender.CUSTOMER -> Sender.CUSTOMER
    Chat.Sender.DRIVER -> Sender.DRIVER
    else -> Sender.DRIVER
}
