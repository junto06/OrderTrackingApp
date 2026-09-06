package com.mudassar.feature.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.os.bundleOf
import com.mudassar.feature.ordertracking.R
import com.mudassar.core.navigation.HasNavigator
import com.mudassar.core.navigation.Navigator
import com.mudassar.core.navigation.navigateTo
import com.mudassar.feature.base.BaseFragment
import com.mudassar.feature.presentation.OrdertrackingFragment

class OrderListFragment : BaseFragment() {

    @Composable
    override fun Content() {
        OrderListScreen { orderId ->
            navigator.navigateTo(
                OrdertrackingFragment().apply {
                    arguments = bundleOf("orderId" to orderId)
                },
            )
        }
    }
}

@Composable
private fun OrderListScreen(onOrderClick: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.orders_title)) })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(fakeOrderIds, key = { id -> id }) { orderId ->
                ListItem(
                    headlineContent = { Text(stringResource(R.string.order_number_format, orderId)) },
                    modifier = Modifier.clickable { onOrderClick(orderId) },
                )
            }
        }
    }
}

private val fakeOrderIds = (1001..1005).map { it.toString() }
