package com.mudassar.ordertracking.server

import io.grpc.ServerBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

fun main() {
    val port = 50051
    val driverScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val trackingServer = ServerBuilder.forPort(port)
        .addService(CustomerTrackingServiceImpl(driverScope))
        .addService(ChatServiceImpl(driverScope))
        .build()

    try {
        trackingServer.start()
        println("RPC order tracking server listening on :$port")
    } catch (e: Exception) {
        System.err.println("FATAL: Failed to start gRPC server on port $port. Is it already in use?")
        e.printStackTrace()
        driverScope.cancel()
        return
    }

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Shutting down gRPC server...")
        trackingServer.shutdown()
        driverScope.cancel()
    })
    trackingServer.awaitTermination()
}
