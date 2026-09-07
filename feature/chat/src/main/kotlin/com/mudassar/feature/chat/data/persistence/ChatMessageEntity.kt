package com.mudassar.feature.chat.data.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val orderId: String,
    val sender: String,
    val text: String,
    val timestamp: Long,
    val syncStatus: SyncStatus
)

enum class SyncStatus {
    PENDING,
    SENT,
    SYNCED
}
