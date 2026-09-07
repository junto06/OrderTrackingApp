package com.mudassar.core.common.di

import com.mudassar.core.base.AppScope
import com.mudassar.core.base.ErrorLogger
import com.mudassar.core.common.logger.FirebaseLogger
import com.mudassar.core.common.rpc.RpcConfigs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {
    @Provides
    @Singleton
    fun provideManagedChannel(rpcConfigs: RpcConfigs): ManagedChannel =
        ManagedChannelBuilder
            .forAddress(rpcConfigs.host, rpcConfigs.port)
            .usePlaintext()
            .build()

    @Provides
    @Singleton
    @AppScope
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Singleton
    @Provides
    fun provideErrorLogger(impl: FirebaseLogger): ErrorLogger = impl
}
