package com.mudassar.core.common.rpc

import com.mudassar.core.rpc.dto.ChatMessageDto
import com.mudassar.core.rpc.dto.ChatSenderDto
import com.mudassar.core.rpc.dto.OrderStatusDto
import com.mudassar.core.rpc.dto.TrackingUpdateDto
import com.mudassar.ordertracking.Chat
import com.mudassar.ordertracking.Tracking
import com.mudassar.ordertracking.Tracking.CustomerEvent
import com.mudassar.ordertracking.Tracking.CustomerEvent.EventCase
import com.mudassar.ordertracking.Tracking.OrderStatus.*

internal fun Chat.ChatMessage.toDto(): ChatMessageDto =
    ChatMessageDto(
        id = id,
        orderId = orderId,
        sender = sender.toDto(),
        text = text,
        timestamp = timestamp,
    )

private fun Chat.Sender.toDto(): ChatSenderDto = when (this) {
    Chat.Sender.CUSTOMER -> ChatSenderDto.CUSTOMER
    Chat.Sender.DRIVER -> ChatSenderDto.DRIVER
    Chat.Sender.SENDER_UNSPECIFIED,
    Chat.Sender.UNRECOGNIZED -> error("Unknown sender")
}

internal fun CustomerEvent.toDto(): TrackingUpdateDto = when (eventCase) {
    EventCase.LOCATION -> TrackingUpdateDto.Location(
        latitude = location.latitude,
        longitude = location.longitude,
        timestamp = location.timestamp,
        etaSeconds = etaSeconds,
    )

    EventCase.STATUS -> TrackingUpdateDto.Status(status.toDto(), etaSeconds = etaSeconds)
    EventCase.EVENT_NOT_SET, null -> error("Received CustomerEvent with no event set")
}

private fun Tracking.OrderStatus.toDto(): OrderStatusDto = when (this) {
    DRIVER_ASSIGNED -> OrderStatusDto.DRIVER_ASSIGNED
    PICKED_UP -> OrderStatusDto.PICKED_UP
    DELIVERED -> OrderStatusDto.DELIVERED
    PENDING_PAYMENT -> OrderStatusDto.PENDING_PAYMENT
    PAYMENT_DECLINED -> OrderStatusDto.PAYMENT_DECLINED
    ORDER_NOT_FOUND -> OrderStatusDto.ORDER_NOT_FOUND
    ORDER_STATUS_UNSPECIFIED,
    UNRECOGNIZED -> OrderStatusDto.UNSPECIFIED
}
