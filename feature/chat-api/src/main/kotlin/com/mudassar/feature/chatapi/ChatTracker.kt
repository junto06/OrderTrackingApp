package com.mudassar.feature.chatapi

interface ChatTracker {
    fun setActiveOrder(orderId: String?)
    fun isChatActive(orderId: String): Boolean
}