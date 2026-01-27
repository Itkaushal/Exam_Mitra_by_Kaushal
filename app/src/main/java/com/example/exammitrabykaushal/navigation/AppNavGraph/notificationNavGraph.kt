package com.example.exammitrabykaushal.navigation.AppNavGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.exammitrabykaushal.UIScreens.screen.NotificationScreen
import com.example.exammitrabykaushal.navigation.Routes

fun NavGraphBuilder.notificationNavGraph(
    navController: NavHostController
) {
    composable(Routes.NOTIFICATION) {
        NotificationScreen(
            onBack = { navController.popBackStack() }
        )
    }
}
