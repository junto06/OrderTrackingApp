package com.mudassar.feature.data

import com.mudassar.core.rpc.dto.OrderStatusDto
import com.mudassar.core.rpc.dto.TrackingUpdateDto
import com.mudassar.feature.domain.OrderStatus
import com.mudassar.feature.domain.TrackingUpdate

internal fun TrackingUpdateDto.toDomain(): TrackingUpdate = when (this) {
    is TrackingUpdateDto.Location -> TrackingUpdate.Location(
        latitude = latitude,
        longitude = longitude,
        timestamp = timestamp,
        etaSeconds = etaSeconds,
    )

    is TrackingUpdateDto.Status -> TrackingUpdate.Status(status.toDomain(), etaSeconds = etaSeconds)
    TrackingUpdateDto.Error -> TrackingUpdate.Error
}

private fun OrderStatusDto.toDomain(): OrderStatus = when (this) {
    OrderStatusDto.DRIVER_ASSIGNED -> OrderStatus.DRIVER_ASSIGNED
    OrderStatusDto.PICKED_UP -> OrderStatus.PICKED_UP
    OrderStatusDto.DELIVERED -> OrderStatus.DELIVERED
    OrderStatusDto.PENDING_PAYMENT -> OrderStatus.PENDING_PAYMENT
    OrderStatusDto.PAYMENT_DECLINED -> OrderStatus.PAYMENT_DECLINED
    OrderStatusDto.ORDER_NOT_FOUND -> OrderStatus.ORDER_NOT_FOUND
    OrderStatusDto.UNSPECIFIED -> OrderStatus.UNSPECIFIED
}
