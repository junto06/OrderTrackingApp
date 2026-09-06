package com.mudassar.feature.presentation

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.mudassar.core.navigation.NavigationEvent.NavigateTo
import com.mudassar.core.navigation.goBack
import com.mudassar.feature.base.BaseFragment
import com.mudassar.feature.chatapi.ChatScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdertrackingFragment : BaseFragment() {
    private val viewModel: OrdertrackingViewModel by viewModels()

    @Composable
    override fun Content() {
        OrdertrackingScreen(
            viewModel = viewModel,
            onBackClick = { navigator.goBack() },
            onOpenChatClick = {
                navigator.navigate(NavigateTo(ChatScreen(it)))
            }
        )
    }
}
