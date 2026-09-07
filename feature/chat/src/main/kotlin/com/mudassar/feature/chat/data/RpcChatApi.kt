package com.mudassar.feature.chat.data

import com.mudassar.core.base.AppScope
import com.mudassar.core.base.ErrorLogger
import com.mudassar.feature.chat.data.persistence.ChatDao
import com.mudassar.feature.chat.notifications.ChatNotificationManager
import com.mudassar.feature.chatapi.ChatInitializer
import com.mudassar.feature.chatapi.ChatTracker
import com.mudassar.ordertracking.Chat
import com.mudassar.ordertracking.ChatServiceGrpcKt.ChatServiceCoroutineStub
import com.mudassar.ordertracking.chatMessage
import io.grpc.ConnectivityState
import io.grpc.ManagedChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

private val RETRY_BASE_DELAY = 1.seconds
private val RETRY_MAX_DELAY = 30.seconds

@Singleton
class RpcChatApi @Inject constructor(
    private val managedChannel: ManagedChannel,
    private val chatDao: ChatDao,
    private val chatTracker: ChatTracker,
    private val notificationManager: ChatNotificationManager,
    @AppScope private val externalScope: CoroutineScope,
    private val errorLogger: ErrorLogger,
) : ChatInitializer {
    private val service = ChatServiceCoroutineStub(managedChannel)

    private val outgoing = MutableSharedFlow<Chat.ChatMessage>(extraBufferCapacity = 16)

    private val incoming: Flow<Chat.ChatMessage> = service.chat(outgoing)
        .retryWhen { cause, attempt ->
            errorLogger.log(cause)
            delay((RETRY_BASE_DELAY * (1 shl attempt.toInt().coerceIn(0, 5))).coerceAtMost(RETRY_MAX_DELAY))
            true
        }
        .shareIn(
            scope = externalScope,
            started = SharingStarted.Eagerly,
            replay = 1,
        )

    init {
        startListeningEagerly()
    }

    // Empty on purpose, calling initChat forces Hilt to create
    // this singleton early, so the message stream connects on app start
    override fun initChat() = Unit

    private fun startListeningEagerly() {
        externalScope.launch {
            try {
                incoming.collect { message ->
                    chatDao.upsertMessage(message.toEntity())
                    
                    if (shouldShowNotification(message)) {
                        notificationManager.showNotification(
                            orderId = message.orderId,
                            messageText = message.text
                        )
                    }
                }
            } catch (e: Exception) {
                errorLogger.log(e)
            }
        }
    }
    private fun shouldShowNotification(message: Chat.ChatMessage): Boolean =
        message.sender == Chat.Sender.DRIVER && !chatTracker.isChatActive(message.orderId)

    suspend fun sendMessage(orderId: String, text: String, id: String) {
        val state = managedChannel.getState(false)
        if (state != ConnectivityState.READY) {
            throw IOException("Chat gRPC stream is not ready (State: $state)")
        }

        outgoing.emit(
            chatMessage {
                this.id = id
                this.orderId = orderId
                sender = Chat.Sender.CUSTOMER
                this.text = text
                timestamp = System.currentTimeMillis()
            },
        )
    }
}
