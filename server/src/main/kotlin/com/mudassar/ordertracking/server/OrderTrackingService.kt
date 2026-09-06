package com.mudassar.ordertracking.server

import com.mudassar.ordertracking.CustomerTrackingServiceGrpcKt.CustomerTrackingServiceCoroutineImplBase
import com.mudassar.ordertracking.Tracking
import com.mudassar.ordertracking.customerEvent
import com.mudassar.ordertracking.driverLocation
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

// Berlin Brandenburg Gate
private val ROUTE = listOf(
    52.5163 to 13.3777,
    52.5165 to 13.3800,
    52.5167 to 13.3825,
    52.5170 to 13.3850,
    52.5174 to 13.3875,
    52.5180 to 13.3900,
    52.5188 to 13.3920,
    52.5195 to 13.3945,
    52.5200 to 13.3970,
    52.5205 to 13.3995,
)
private val STEP_INTERVAL = 6.seconds
private val DISPATCH_DELAY = 4.seconds

class CustomerTrackingServiceImpl(
    private val driverScope: CoroutineScope,
) : CustomerTrackingServiceCoroutineImplBase() {

    private val driverChannels = ConcurrentHashMap<String, DriverChannel>()

    override fun subscribe(request: Tracking.SubscribeRequest): Flow<Tracking.CustomerEvent> =
        handleRequest(request)

    private fun handleRequest(request: Tracking.SubscribeRequest): Flow<Tracking.CustomerEvent> =
        when (request.orderId) {
            "1001" -> flowOf(statusEvent(Tracking.OrderStatus.PENDING_PAYMENT))
            "1002" -> flowOf(statusEvent(Tracking.OrderStatus.PAYMENT_DECLINED))
            "1003" -> flowOf(statusEvent(Tracking.OrderStatus.DELIVERED))
            "1004" -> flowOf(statusEvent(Tracking.OrderStatus.ORDER_NOT_FOUND))
            else -> defaultTrackingFlow(request.orderId)
        }

    private fun defaultTrackingFlow(orderId: String): Flow<Tracking.CustomerEvent> =
        driverChannels.computeIfAbsent(orderId) { id ->
            DriverChannel().also { channel ->
                driverScope.launch { simulateDriver(id, channel) }
            }
        }.asEventFlow()

    private suspend fun simulateDriver(orderId: String, channel: DriverChannel) {
        try {
            channel.statusUpdates.emit(Tracking.OrderStatus.DRIVER_ASSIGNED)

            delay(DISPATCH_DELAY)

            ROUTE.forEachIndexed { index, (latitude, longitude) ->
                channel.locationUpdates.emit(
                    driverLocation {
                        this.latitude = latitude
                        this.longitude = longitude
                        timestamp = System.currentTimeMillis()
                    },
                )
                if (index != ROUTE.lastIndex) delay(STEP_INTERVAL)
            }

            channel.statusUpdates.emit(Tracking.OrderStatus.DELIVERED)
        } finally {
            driverChannels.remove(orderId)
        }
    }

    private fun statusEvent(event: Tracking.OrderStatus) = customerEvent { status = event }
}

// dispatch delay 4s + 9 * 6s between the 10 route points
private const val TRIP_DURATION_SECONDS = 58

private class DriverChannel {
    private val tripStartTimeMillis = System.currentTimeMillis()

    val statusUpdates = MutableSharedFlow<Tracking.OrderStatus>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val locationUpdates = MutableSharedFlow<Tracking.DriverLocation>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun asEventFlow(): Flow<Tracking.CustomerEvent> = merge(
        statusUpdates.map {
            customerEvent {
                status = it
                etaSeconds = remainingSeconds()
            }
        },
        locationUpdates.map {
            customerEvent {
                location = it
                etaSeconds = remainingSeconds()
            }
        },
    )

    private fun remainingSeconds(): Int {
        val elapsedSeconds = (System.currentTimeMillis() - tripStartTimeMillis) / 1000
        return (TRIP_DURATION_SECONDS - elapsedSeconds).coerceAtLeast(0).toInt()
    }
}
