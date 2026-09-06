package com.mudassar.feature.chat.data

import com.mudassar.core.rpc.ChatApi
import com.mudassar.feature.chat.domain.ChatMessage
import com.mudassar.feature.chat.domain.ChatRepository
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val MESSAGE_CACHE_SIZE = 200
private const val MAX_CACHED_ORDERS = 5

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val chatApi: ChatApi,
) : ChatRepository {

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private class CachedOrder(val messages: MutableSharedFlow<ChatMessage>, val job: Job)

    //the comms layer for an order stays alive in the background regardless of
    //whether its chat screen is open, but only for the most recently touched orders
    //evicted order's gRPC stream is cancelled instead of running forever unattended.
    private val messageCache = Collections.synchronizedMap(
        object : LinkedHashMap<String, CachedOrder>(MAX_CACHED_ORDERS, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, CachedOrder>): Boolean {
                if (size <= MAX_CACHED_ORDERS) return false
                eldest.value.job.cancel()
                return true
            }
        },
    )

    override fun observeMessages(orderId: String): Flow<ChatMessage> =
        cacheFor(orderId).messages.asSharedFlow()

    override suspend fun sendMessage(orderId: String, text: String) {
        chatApi.sendMessage(orderId, text)
    }

    private fun cacheFor(orderId: String): CachedOrder =
        messageCache.computeIfAbsent(orderId) { id ->
            val messages = MutableSharedFlow<ChatMessage>(replay = MESSAGE_CACHE_SIZE)
            val job = repositoryScope.launch {
                chatApi.observeMessages(id)
                    .map { it.toDomain() }
                    .collect {
                        messages.emit(it)
                    }
            }
            CachedOrder(messages, job)
        }
}
