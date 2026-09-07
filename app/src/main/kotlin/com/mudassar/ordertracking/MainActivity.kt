package com.mudassar.ordertracking

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.mudassar.core.navigation.HasNavigator
import com.mudassar.core.navigation.Navigator
import com.mudassar.core.navigation.navigateTo
import com.mudassar.feature.chatapi.openChat
import com.mudassar.feature.presentation.OrderListFragment
import com.mudassar.ordertracking.navigation.NavigatorImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity(), HasNavigator {

    override val navigator: Navigator = NavigatorImpl(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (savedInstanceState == null) {
            navigator.navigateTo(OrderListFragment(), backStack = false)
            handleChatIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleChatIntent(intent)
    }

    private fun handleChatIntent(intent: Intent?) {
        intent?.getStringExtra("orderId")?.let {
            navigator.openChat(it)
        }
    }
}
