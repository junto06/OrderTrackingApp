package com.mudassar.feature.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mudassar.feature.domain.OrderId
import com.mudassar.feature.domain.OrderRepository
import com.mudassar.feature.domain.TrackingUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class OrdertrackingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    orderRepository: OrderRepository,
) : ViewModel() {
    private val orderId = OrderId(checkNotNull(savedStateHandle["orderId"]))
    val orderIdValue: String get() = orderId.value

    val uiState: StateFlow<OrdertrackingUiState> =
        orderRepository.trackOrder(orderId)
            .scan(OrdertrackingUiState.Loading as OrdertrackingUiState) { state, update -> state.reduce(update) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = OrdertrackingUiState.Loading,
            )
}

private fun OrdertrackingUiState.reduce(update: TrackingUpdate): OrdertrackingUiState {
    val content = this as? OrdertrackingUiState.Content ?: OrdertrackingUiState.Content()
    return when (update) {
        is TrackingUpdate.Location -> content.copy(location = update, etaSeconds = update.etaSeconds)
        is TrackingUpdate.Status -> content.copy(status = update.status, etaSeconds = update.etaSeconds)
        TrackingUpdate.Error -> OrdertrackingUiState.Error
    }
}
