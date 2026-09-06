package com.mudassar.core.rpc.dto

sealed interface TrackingUpdateDto {
    data class Location(
        val latitude: Double,
        val longitude: Double,
        val timestamp: Long,
        val etaSeconds: Int,
    ) : TrackingUpdateDto

    data class Status(val status: OrderStatusDto, val etaSeconds: Int) : TrackingUpdateDto

    data object Error : TrackingUpdateDto
}

enum class OrderStatusDto {
    UNSPECIFIED,
    DRIVER_ASSIGNED,
    PICKED_UP,
    DELIVERED,
    PENDING_PAYMENT,
    PAYMENT_DECLINED,
    ORDER_NOT_FOUND,
}
