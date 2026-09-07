package com.mudassar.feature.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.mudassar.core.navigation.goBack
import com.mudassar.feature.base.BaseFragment
import com.mudassar.feature.chatapi.openChat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdertrackingFragment : BaseFragment() {
    private val viewModel: OrdertrackingViewModel by viewModels()

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    @Composable
    override fun Content() {
        OrdertrackingScreen(
            viewModel = viewModel,
            onBackClick = { navigator.goBack() },
            onOpenChatClick = {
                requestNotificationPermissionIfNeeded()
                navigator.openChat(it)
            }
        )
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
