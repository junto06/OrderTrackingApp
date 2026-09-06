package com.mudassar.feature.presentation

import com.mudassar.feature.domain.OrderStatus
import com.mudassar.feature.domain.TrackingUpdate

sealed interface OrdertrackingUiState {
    data object Loading : OrdertrackingUiState

    data class Content(
        val location: TrackingUpdate.Location? = null,
        val status: OrderStatus = OrderStatus.UNSPECIFIED,
        val etaSeconds: Int? = null,
    ) : OrdertrackingUiState

    data object Error : OrdertrackingUiState
}


