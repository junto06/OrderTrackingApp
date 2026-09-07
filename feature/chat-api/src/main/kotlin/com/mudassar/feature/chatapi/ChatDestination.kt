package com.mudassar.feature.chatapi

import com.mudassar.core.navigation.NavigationEvent.NavigateTo
import com.mudassar.core.navigation.NavigationEvent.NavigationDestination
import com.mudassar.core.navigation.Navigator

sealed interface ChatDestination : NavigationDestination.CustomDestination

data class ChatScreen(val orderId: String) : ChatDestination

fun Navigator.openChat(orderId: String) =
    navigate(NavigateTo(ChatScreen(orderId)))