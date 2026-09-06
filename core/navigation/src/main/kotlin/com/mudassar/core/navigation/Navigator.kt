package com.mudassar.core.navigation

import androidx.fragment.app.Fragment
import com.mudassar.core.navigation.NavigationEvent.NavigateTo
import com.mudassar.core.navigation.NavigationEvent.NavigationDestination.ToFragment

interface Navigator {
    fun navigate(event: NavigationEvent)
}

fun Navigator.navigateTo(fragment: Fragment, backStack: Boolean = true) =
    navigate(NavigateTo(ToFragment(fragment, addToBackStack = backStack)))

fun Navigator.goBack() =
    navigate(NavigationEvent.NavigateBack)

interface HasNavigator {
    val navigator: Navigator
}

sealed interface NavigationEvent {
    data class NavigateTo(val destination: NavigationDestination) : NavigationEvent
    object NavigateBack : NavigationEvent

    sealed interface NavigationDestination {
        data class ToFragment(
            val fragment: Fragment,
            val addToBackStack: Boolean = false
        ) : NavigationDestination

        interface CustomDestination : NavigationDestination
    }
}