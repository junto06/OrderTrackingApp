package com.mudassar.feature.data

import com.mudassar.core.base.ErrorLogger
import com.mudassar.feature.domain.OrderId
import com.mudassar.feature.domain.OrderRepository
import com.mudassar.feature.domain.TrackingUpdate
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val rpcOrderTrackingApi: RpcOrderTrackingApi,
    private val errorLogger: ErrorLogger,
) : OrderRepository {
    override fun trackOrder(orderId: OrderId): Flow<TrackingUpdate> =
        rpcOrderTrackingApi.trackOrder(orderId.value)
            .map { it.toDomain() }
            .catch { e ->
                errorLogger.log(e)
                emit(TrackingUpdate.Error)
            }
}
