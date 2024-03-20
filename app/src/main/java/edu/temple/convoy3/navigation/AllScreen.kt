package edu.temple.convoy3.navigation

import edu.temple.convoy3.screens.SignUpScreen
import java.lang.IllegalArgumentException

enum class AllScreen {
    MainScreen,
    SignInScreen,
    SignUpScreen;
    companion object {
        fun fromRoute(route: String?): AllScreen
        = when (route?.substringBefore("/")) {
            MainScreen.name -> MainScreen
            SignInScreen.name -> SignInScreen
            SignUpScreen.name -> SignUpScreen
            null -> SignInScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}