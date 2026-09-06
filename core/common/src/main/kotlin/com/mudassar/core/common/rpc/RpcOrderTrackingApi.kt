package com.mudassar.core.common.rpc

import com.mudassar.core.rpc.OrderTrackingApi
import com.mudassar.core.rpc.dto.TrackingUpdateDto
import com.mudassar.ordertracking.CustomerTrackingServiceGrpcKt.CustomerTrackingServiceCoroutineStub
import com.mudassar.ordertracking.subscribeRequest
import io.grpc.ManagedChannelBuilder
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@Singleton
class RpcOrderTrackingApi @Inject constructor(private val rpcConfigs: RpcConfigs) :
    OrderTrackingApi {
    private val service = CustomerTrackingServiceCoroutineStub(
        ManagedChannelBuilder
            .forAddress(rpcConfigs.host, rpcConfigs.port)
            .usePlaintext()
            .build(),
    )

    override fun trackOrder(orderId: String): Flow<TrackingUpdateDto> =
        service.subscribe(subscribeRequest { this.orderId = orderId })
            .map { it.toDto() }
            .catch { emit(TrackingUpdateDto.Error) }
}
