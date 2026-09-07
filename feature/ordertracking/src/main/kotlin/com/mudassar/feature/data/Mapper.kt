package com.mudassar.feature.data

import com.mudassar.feature.domain.OrderStatus
import com.mudassar.feature.domain.TrackingUpdate
import com.mudassar.ordertracking.Tracking
import com.mudassar.ordertracking.Tracking.CustomerEvent
import com.mudassar.ordertracking.Tracking.CustomerEvent.EventCase
import com.mudassar.ordertracking.Tracking.OrderStatus.*

internal fun CustomerEvent.toDomain(): TrackingUpdate = when (eventCase) {
    EventCase.LOCATION -> TrackingUpdate.Location(
        latitude = location.latitude,
        longitude = location.longitude,
        timestamp = location.timestamp,
        etaSeconds = etaSeconds,
    )

    EventCase.STATUS -> TrackingUpdate.Status(status.toDomain(), etaSeconds = etaSeconds)
    EventCase.EVENT_NOT_SET, null -> TrackingUpdate.Error
}

private fun Tracking.OrderStatus.toDomain(): OrderStatus = when (this) {
    DRIVER_ASSIGNED -> OrderStatus.DRIVER_ASSIGNED
    PICKED_UP -> OrderStatus.PICKED_UP
    DELIVERED -> OrderStatus.DELIVERED
    PENDING_PAYMENT -> OrderStatus.PENDING_PAYMENT
    PAYMENT_DECLINED -> OrderStatus.PAYMENT_DECLINED
    ORDER_NOT_FOUND -> OrderStatus.ORDER_NOT_FOUND
    ORDER_STATUS_UNSPECIFIED,
    UNRECOGNIZED -> OrderStatus.UNSPECIFIED
}
