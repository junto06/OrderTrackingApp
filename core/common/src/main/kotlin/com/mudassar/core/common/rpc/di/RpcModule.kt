package com.mudassar.core.common.rpc.di

import com.mudassar.core.common.rpc.RpcChatApi
import com.mudassar.core.common.rpc.RpcOrderTrackingApi
import com.mudassar.core.rpc.ChatApi
import com.mudassar.core.rpc.OrderTrackingApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RpcModule {
    @Binds
    fun bindOrderTrackingApi(impl: RpcOrderTrackingApi): OrderTrackingApi

    @Binds
    fun bindChatApi(impl: RpcChatApi): ChatApi
}