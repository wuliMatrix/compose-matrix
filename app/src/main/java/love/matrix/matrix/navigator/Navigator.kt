package com.instagram.clone.navigator

import com.instagram.clone.models.User222

sealed class Navigator {
    object HomePage : Navigator()
    data class DetailPage(val user: User222) : Navigator()
    object InfoPage : Navigator()
}


object AppNavigation {
    var currentScreen: Navigator = Navigator.HomePage
}

fun navigateTo(destination: Navigator) {
    AppNavigation.currentScreen = destination
}