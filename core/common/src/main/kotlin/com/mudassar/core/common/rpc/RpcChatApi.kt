package com.mudassar.core.common.rpc

import com.mudassar.core.rpc.ChatApi
import com.mudassar.core.rpc.dto.ChatMessageDto
import com.mudassar.ordertracking.Chat
import com.mudassar.ordertracking.ChatServiceGrpcKt.ChatServiceCoroutineStub
import com.mudassar.ordertracking.chatMessage
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RpcChatApi @Inject constructor(private val rpcConfigs: RpcConfigs) : ChatApi {
    private val service = ChatServiceCoroutineStub(
        ManagedChannelBuilder
            .forAddress(rpcConfigs.host, rpcConfigs.port)
            .usePlaintext()
            .build(),
    )

    private val outgoing = MutableSharedFlow<Chat.ChatMessage>(extraBufferCapacity = 16)
    private val incoming: Flow<Chat.ChatMessage> by lazy { service.chat(outgoing) }

    override fun observeMessages(orderId: String): Flow<ChatMessageDto> =
        incoming
            .filter { it.orderId == orderId }
            .map { it.toDto() }

    override suspend fun sendMessage(orderId: String, text: String) {
        outgoing.emit(
            chatMessage {
                id = UUID.randomUUID().toString()
                this.orderId = orderId
                sender = Chat.Sender.CUSTOMER
                this.text = text
                timestamp = System.currentTimeMillis()
            },
        )
    }
}
