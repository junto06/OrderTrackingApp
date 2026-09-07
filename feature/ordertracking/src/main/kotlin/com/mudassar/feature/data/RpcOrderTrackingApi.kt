package com.mudassar.feature.data

import com.mudassar.ordertracking.CustomerTrackingServiceGrpcKt.CustomerTrackingServiceCoroutineStub
import com.mudassar.ordertracking.Tracking
import com.mudassar.ordertracking.subscribeRequest
import io.grpc.ManagedChannel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RpcOrderTrackingApi @Inject constructor(managedChannel: ManagedChannel) {
    private val service = CustomerTrackingServiceCoroutineStub(managedChannel)

    fun trackOrder(orderId: String): Flow<Tracking.CustomerEvent> =
        service.subscribe(subscribeRequest { this.orderId = orderId })
}
