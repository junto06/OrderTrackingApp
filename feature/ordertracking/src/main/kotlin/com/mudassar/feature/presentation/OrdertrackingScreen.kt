package com.mudassar.feature.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mudassar.feature.domain.OrderStatus
import com.mudassar.feature.ordertracking.R
import com.mudassar.feature.base.R as BaseR

@Composable
fun OrdertrackingScreen(
    viewModel: OrdertrackingViewModel,
    onBackClick: () -> Unit,
    onOpenChatClick: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.track_order_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(BaseR.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val state = uiState) {
                OrdertrackingUiState.Loading -> LoadingPage()

                OrdertrackingUiState.Error -> ErrorPage()

                is OrdertrackingUiState.Content ->
                    PageContent(state, onChatClick = { onOpenChatClick(viewModel.orderIdValue) })
            }
        }
    }
}

@Composable
private fun PageContent(state: OrdertrackingUiState.Content, onChatClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        val mapModifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        if (state.location == null && !state.status.tracksLocation()) {
            Box(modifier = mapModifier)
        } else {
            TrackingMap(location = state.location, modifier = mapModifier)
        }
        StatusBar(status = state.status, etaSeconds = state.etaSeconds, onChatClick = onChatClick)
    }
}

// Orders that resolve to payment issues, an instant delivery, an unknown order have no
// map data to show an empty box reads better than a spinner that will never resolve.
private fun OrderStatus.tracksLocation(): Boolean =
    this == OrderStatus.UNSPECIFIED || this == OrderStatus.DRIVER_ASSIGNED || this == OrderStatus.PICKED_UP

@Composable
private fun ErrorPage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.tracking_error),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
private fun LoadingPage() {
    Box(
        Modifier
            .fillMaxSize()
            .navigationBarsPadding(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

private const val TripDurationSeconds = 58

@Composable
private fun StatusBar(status: OrderStatus, etaSeconds: Int?, onChatClick: () -> Unit) {
    val isInTransit = status == OrderStatus.DRIVER_ASSIGNED || status == OrderStatus.PICKED_UP

    Surface(shadowElevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = status.toDisplayText(),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(16.dp))
                SmallFloatingActionButton(onClick = onChatClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = stringResource(R.string.chat_with_driver),
                    )
                }
            }

            if (isInTransit && etaSeconds != null) {
                val progress = (1f - etaSeconds.toFloat() / TripDurationSeconds).coerceIn(0f, 1f)

                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = etaSeconds.toRemainingTimeText(),
                    style = MaterialTheme.typography.bodySmall
                )
            } else if (status == OrderStatus.DELIVERED) {
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(progress = { 1f }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = stringResource(R.string.arrived), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun Int.toRemainingTimeText(): String {
    val minutes = this / 60
    val seconds = this % 60
    return stringResource(R.string.remaining_time_format, minutes, seconds)
}

@Composable
private fun OrderStatus.toDisplayText(): String = when (this) {
    OrderStatus.UNSPECIFIED -> stringResource(R.string.status_waiting_for_updates)
    OrderStatus.DRIVER_ASSIGNED -> stringResource(R.string.status_driver_assigned)
    OrderStatus.PICKED_UP -> stringResource(R.string.status_picked_up)
    OrderStatus.DELIVERED -> stringResource(R.string.status_delivered)
    OrderStatus.PENDING_PAYMENT -> stringResource(R.string.status_payment_pending)
    OrderStatus.PAYMENT_DECLINED -> stringResource(R.string.status_payment_declined)
    OrderStatus.ORDER_NOT_FOUND -> stringResource(R.string.status_order_not_found)
}
