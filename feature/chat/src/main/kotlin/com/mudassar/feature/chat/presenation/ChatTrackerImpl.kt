package com.mudassar.feature.chat.presenation

import com.mudassar.feature.chatapi.ChatTracker
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatTrackerImpl @Inject constructor() : ChatTracker {
    private var activeOrderId: String? = null

    override fun setActiveOrder(orderId: String?) {
        activeOrderId = orderId
    }

    override fun isChatActive(orderId: String): Boolean {
        return activeOrderId == orderId
    }
}