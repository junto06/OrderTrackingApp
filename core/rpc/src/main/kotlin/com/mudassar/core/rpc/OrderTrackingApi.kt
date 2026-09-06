package com.mudassar.core.rpc

import com.mudassar.core.rpc.dto.TrackingUpdateDto
import kotlinx.coroutines.flow.Flow

interface OrderTrackingApi {
    fun trackOrder(orderId: String): Flow<TrackingUpdateDto>
}