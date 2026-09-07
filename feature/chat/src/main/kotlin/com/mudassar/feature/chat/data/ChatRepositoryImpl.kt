package com.mudassar.feature.chat.data

import com.mudassar.core.base.ErrorLogger
import com.mudassar.feature.chat.data.persistence.ChatDao
import com.mudassar.feature.chat.data.persistence.ChatMessageEntity
import com.mudassar.feature.chat.data.persistence.SyncStatus
import com.mudassar.feature.chat.domain.ChatMessage
import com.mudassar.feature.chat.domain.ChatRepository
import com.mudassar.feature.chat.domain.ChatSyncScheduler
import com.mudassar.feature.chat.domain.Sender
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val rpcChatApi: RpcChatApi,
    private val chatDao: ChatDao,
    private val chatSyncScheduler: ChatSyncScheduler,
    private val errorLogger: ErrorLogger,
) : ChatRepository {

    private val syncMutex = Mutex()

    override fun observeMessages(orderId: String): Flow<List<ChatMessage>> =
        chatDao.getMessagesForOrder(orderId)
            .map { list -> list.map { it.toDomain() } }

    override suspend fun sendMessage(orderId: String, text: String) {
        val messageId = UUID.randomUUID().toString()
        val pendingMessage = ChatMessageEntity(
            id = messageId,
            orderId = orderId,
            sender = Sender.CUSTOMER.name,
            text = text,
            timestamp = System.currentTimeMillis(),
            syncStatus = SyncStatus.PENDING
        )

        chatDao.upsertMessage(pendingMessage)

        val success = sendMessage(pendingMessage)
        if (!success) {
            chatSyncScheduler.scheduleSync()
        }
    }

    override suspend fun syncPendingMessages(): Boolean = syncMutex.withLock {
        val pendingMessages = chatDao.getPendingMessages()
        if (pendingMessages.isEmpty()) return@withLock true
        buildList {
            for (message in pendingMessages) {
                add(sendMessage(message))
            }
        }.all { it }
    }

    private suspend fun sendMessage(message: ChatMessageEntity): Boolean {
        return try {
            rpcChatApi.sendMessage(
                orderId = message.orderId,
                text = message.text,
                id = message.id //idempotencyKey
            )
            chatDao.upsertMessage(
                message.copy(
                    syncStatus = SyncStatus.SENT
                )
            )
            true
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            errorLogger.log(e)
            false
        }
    }
}
