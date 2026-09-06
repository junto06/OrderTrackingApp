package com.mudassar.feature.chatapi

import com.mudassar.core.navigation.NavigationEvent.NavigationDestination

sealed interface ChatDestination : NavigationDestination.CustomDestination

data class ChatScreen(val orderId: String) : ChatDestination