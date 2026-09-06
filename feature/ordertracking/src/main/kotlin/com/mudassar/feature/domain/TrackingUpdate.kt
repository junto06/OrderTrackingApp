package com.mudassar.feature.domain

sealed interface TrackingUpdate {
    data class Location(
        val latitude: Double,
        val longitude: Double,
        val timestamp: Long,
        val etaSeconds: Int,
    ) : TrackingUpdate

    data class Status(val status: OrderStatus, val etaSeconds: Int) : TrackingUpdate
    data object Error : TrackingUpdate
}

enum class OrderStatus {
    UNSPECIFIED,
    DRIVER_ASSIGNED,
    PICKED_UP,
    DELIVERED,
    PENDING_PAYMENT,
    PAYMENT_DECLINED,
    ORDER_NOT_FOUND,
}
