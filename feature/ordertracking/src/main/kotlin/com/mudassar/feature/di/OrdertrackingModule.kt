package com.mudassar.feature.di

import com.mudassar.feature.data.OrderRepositoryImpl
import com.mudassar.feature.domain.OrderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class OrdertrackingModule {
    @Binds
    abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository
}
