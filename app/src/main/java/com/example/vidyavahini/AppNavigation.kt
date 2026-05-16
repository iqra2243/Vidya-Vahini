package com.example.vidyavahini

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

@Composable
fun AppNavigation() {

    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }

        composable("home") {
            HomeScreen(navController)
        }

        composable("route") {
            RouteScreen(navController)
        }

        composable("ping/{routeIndex}") { backStackEntry ->
            val routeIndex = backStackEntry.arguments
                ?.getString("routeIndex")
                ?.toIntOrNull() ?: 0

            PingScreen(navController, routeIndex)
        }

        composable("report") {
            ReportScreen()
        }

        composable("safe") {
            SafeScreen()
        }

        composable("history") {
            HistoryScreen()
        }

        composable("delay") {
            DelayScreen()
        }
    }
}