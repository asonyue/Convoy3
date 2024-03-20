package edu.temple.convoy3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.temple.convoy3.screens.MainScreen
import edu.temple.convoy3.screens.SignInScreen
import edu.temple.convoy3.screens.SignUpScreen

@Composable
fun AllNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AllScreen.SignInScreen.name) {
        composable(AllScreen.SignInScreen.name) {
            SignInScreen(navController)
        }
        composable(AllScreen.SignUpScreen.name) {
            SignUpScreen(navController)
        }
        composable(AllScreen.MainScreen.name) {
            MainScreen(navController)
        }
    }
}