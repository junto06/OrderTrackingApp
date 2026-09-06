package com.mudassar.feature.data

import com.mudassar.core.rpc.OrderTrackingApi
import com.mudassar.feature.domain.OrderId
import com.mudassar.feature.domain.OrderRepository
import com.mudassar.feature.domain.TrackingUpdate
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val orderTrackingApi: OrderTrackingApi,
) : OrderRepository {
    override fun trackOrder(orderId: OrderId): Flow<TrackingUpdate> =
        orderTrackingApi.trackOrder(orderId.value).map { it.toDomain() }
}
