package com.mudassar.feature.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.mudassar.core.navigation.HasNavigator
import com.mudassar.core.navigation.Navigator
import com.mudassar.feature.base.theme.OrderTrackingTheme

abstract class BaseFragment : Fragment(), HasNavigator {
    @Composable
    abstract fun Content()

    override val navigator: Navigator
        get() = (requireActivity() as HasNavigator).navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setContent { OrderTrackingTheme { this@BaseFragment.Content() } }
    }
}
