package com.mudassar.ordertracking.server

import com.mudassar.ordertracking.Chat
import com.mudassar.ordertracking.ChatServiceGrpcKt.ChatServiceCoroutineImplBase
import com.mudassar.ordertracking.chatMessage
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatServiceImpl(
    private val driverScope: CoroutineScope,
) : ChatServiceCoroutineImplBase() {

    private val chatFlow = MutableSharedFlow<Chat.ChatMessage>(extraBufferCapacity = 64)
    private val latestOrderId = AtomicReference<String?>(null)

    init {
        initDriveChat()
    }

    override fun chat(requests: Flow<Chat.ChatMessage>): Flow<Chat.ChatMessage> {
        driverScope.launch {
            requests.collect { message ->
                chatFlow.emit(message)
                if (message.sender == Chat.Sender.CUSTOMER) {
                    latestOrderId.set(message.orderId)
                    println("\n[CUSTOMER] ${message.orderId}: ${message.text}")
                }
            }
        }
        return chatFlow.asSharedFlow()
    }

    private fun initDriveChat() = driverScope.launch {
        while (true) {
            val orderId = latestOrderId.get()
            if (orderId == null) {
                val id = prompt("Enter Order ID to start chat: ")
                if (!id.isNullOrBlank()) {
                    latestOrderId.set(id)
                }
                continue
            }

            val input = prompt("Send to $orderId (type 'new' to switch): ")
            when {
                input == null -> break
                input.lowercase() == "new" -> {
                    latestOrderId.set(null)
                }
                input.isNotBlank() -> {
                    chatFlow.emit(
                        chatMessage {
                            this.id = UUID.randomUUID().toString()
                            this.orderId = orderId
                            sender = Chat.Sender.DRIVER
                            text = input
                            timestamp = System.currentTimeMillis()
                        },
                    )
                }
            }
        }
    }

    private suspend fun prompt(message: String): String? = withContext(Dispatchers.IO) {
        print("\n$message")
        readlnOrNull()
    }
}
