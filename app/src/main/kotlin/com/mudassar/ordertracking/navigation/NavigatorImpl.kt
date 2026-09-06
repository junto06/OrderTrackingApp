package com.mudassar.ordertracking.navigation

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.mudassar.core.navigation.NavigationEvent
import com.mudassar.core.navigation.NavigationEvent.NavigationDestination
import com.mudassar.core.navigation.Navigator
import com.mudassar.feature.chat.presenation.ChatFragment
import com.mudassar.feature.chatapi.ChatScreen

class NavigatorImpl(private val fragmentManager: FragmentManager) : Navigator {

    override fun navigate(event: NavigationEvent) {
        when (event) {
            NavigationEvent.NavigateBack -> handlePopBackStack()
            is NavigationEvent.NavigateTo -> handleNavigation(event)
        }
    }

    private fun handleNavigation(event: NavigationEvent.NavigateTo) {
        when (val destination = event.destination) {
            is NavigationDestination.ToFragment -> {
                addFragment(destination.fragment, destination.addToBackStack)
            }

            is NavigationDestination.CustomDestination if (destination is ChatScreen) -> {
                addFragment(
                    ChatFragment().apply {
                        arguments = bundleOf("orderId" to destination.orderId)
                    }, addToBackStack = true
                )
            }

            else -> {
                error("Unknown destination: $destination")
            }
        }
    }

    private fun handlePopBackStack() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            error("No backstack to pop")
        }
    }

    private fun addFragment(fragment: Fragment, addToBackStack: Boolean) {
        fragmentManager.commit {
            replace(android.R.id.content, fragment)
            if (addToBackStack) {
                addToBackStack(null)
            }
        }
    }
}