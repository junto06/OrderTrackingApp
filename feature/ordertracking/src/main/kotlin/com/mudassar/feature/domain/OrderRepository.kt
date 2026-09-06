package com.mudassar.feature.domain

import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun trackOrder(orderId: OrderId): Flow<TrackingUpdate>
}
