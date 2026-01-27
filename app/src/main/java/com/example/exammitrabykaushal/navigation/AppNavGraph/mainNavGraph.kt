package com.example.exammitrabykaushal.navigation.AppNavGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.exammitrabykaushal.UIScreens.screen.DashboardScreen
import com.example.exammitrabykaushal.UIScreens.screen.NotificationScreen
import com.example.exammitrabykaushal.UIScreens.screen.ProfileScreen
import com.example.exammitrabykaushal.UIScreens.screen.TestHistoryScreen
import com.example.exammitrabykaushal.navigation.Routes

fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {

    composable(Routes.DASHBOARD) {
        DashboardScreen(
            onNavigateToTest = {
                navController.navigate(Routes.quiz(it))
            },
            onNavigateToProfile = {
                navController.navigate(Routes.PROFILE)
            },
            onNavigateToVideoLectures = {
                navController.navigate(Routes.VIDEO_HOME)
            },
            onNavigateToPYQ = {
                navController.navigate(Routes.PYQ_SELECTION)
            },
            onNavigateToNineToTwelve = {
                navController.navigate(Routes.NINE_TO_TWELVE_HOME)
            }
        )
    }

    composable(Routes.PROFILE) {
        ProfileScreen(
            onBack = { navController.popBackStack() },
            onLogout = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            },
            onNavigateToHistory = {
                navController.navigate(Routes.TEST_HISTORY)
            },
            onNavigateToNotification = {
                navController.navigate(Routes.NOTIFICATION)
            }
        )
    }

    composable(Routes.TEST_HISTORY) {
        TestHistoryScreen(
            onBack = { navController.popBackStack() }
        )
    }

    composable(Routes.NOTIFICATION) {
        NotificationScreen { navController.popBackStack() }
    }
}
