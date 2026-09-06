package com.mudassar.ordertracking.server

import com.mudassar.ordertracking.Chat
import com.mudassar.ordertracking.ChatServiceGrpcKt.ChatServiceCoroutineImplBase
import com.mudassar.ordertracking.chatMessage
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

private val DRIVER_REPLIES = listOf(
    "On my way",
    "Just parked coming up now",
    "Thanks see you soon",
    "Got it",
)
private val DRIVER_REPLY_DELAY = 2.seconds

class ChatServiceImpl(
    private val driverScope: CoroutineScope,
) : ChatServiceCoroutineImplBase() {

    private val broadcast = MutableSharedFlow<Chat.ChatMessage>(extraBufferCapacity = 64)

    override fun chat(requests: Flow<Chat.ChatMessage>): Flow<Chat.ChatMessage> {
        driverScope.launch {
            requests.collect { message ->
                broadcast.emit(message)
                if (message.sender == Chat.Sender.CUSTOMER) {
                    driverScope.launch { replyAsDriver(message.orderId) }
                }
            }
        }
        return broadcast.asSharedFlow()
    }

    private suspend fun replyAsDriver(orderId: String) {
        delay(DRIVER_REPLY_DELAY)
        broadcast.emit(
            chatMessage {
                id = UUID.randomUUID().toString()
                this.orderId = orderId
                sender = Chat.Sender.DRIVER
                text = DRIVER_REPLIES.random()
                timestamp = System.currentTimeMillis()
            },
        )
    }
}
