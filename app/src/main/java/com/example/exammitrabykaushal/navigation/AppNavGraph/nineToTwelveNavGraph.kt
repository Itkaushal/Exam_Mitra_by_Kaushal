package com.example.exammitrabykaushal.navigation.AppNavGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.exammitrabykaushal.UIScreens.nineToTwelve.Class10Screen
import com.example.exammitrabykaushal.UIScreens.nineToTwelve.Class11Screen
import com.example.exammitrabykaushal.UIScreens.nineToTwelve.Class12Screen
import com.example.exammitrabykaushal.UIScreens.nineToTwelve.Class9Screen
import com.example.exammitrabykaushal.UIScreens.nineToTwelve.ClassSubjectScreen
import com.example.exammitrabykaushal.UIScreens.nineToTwelve.NineToTwelveHomeScreen
import com.example.exammitrabykaushal.navigation.Routes

fun NavGraphBuilder.nineToTwelveNavGraph(
    navController: NavHostController
) {

    // Class 9–12 Home
    composable(Routes.NINE_TO_TWELVE_HOME) {
        NineToTwelveHomeScreen(
            onClassSelected = { className ->
                when (className) {
                    "Class 9" -> navController.navigate(Routes.CLASS_9)
                    "Class 10" -> navController.navigate(Routes.CLASS_10)
                    "Class 11" -> navController.navigate(Routes.CLASS_11)
                    "Class 12" -> navController.navigate(Routes.CLASS_12)
                }
            },
            onBack = { navController.popBackStack() }
        )
    }

    // Class 9
    composable(Routes.CLASS_9) {
        Class9Screen(
            onSubjectSelected = { subject ->
                navController.navigate(Routes.classSubject("9", subject))
            },
            onBack = { navController.popBackStack() }
        )
    }

    // Class 10
    composable(Routes.CLASS_10) {
        Class10Screen(
            onSubjectSelected = { subject ->
                navController.navigate(Routes.classSubject("10", subject))
            },
            onBack = { navController.popBackStack() }
        )
    }

    // Class 11
    composable(Routes.CLASS_11) {
        Class11Screen(
            onSubjectSelected = { subject ->
                navController.navigate(Routes.classSubject("11", subject))
            },
            onBack = { navController.popBackStack() }
        )
    }

    // Class 12
    composable(Routes.CLASS_12) {
        Class12Screen(
            onSubjectSelected = { subject ->
                navController.navigate(Routes.classSubject("12", subject))
            },
            onBack = { navController.popBackStack() }
        )
    }

    // Subject-wise screen (shared)
    composable(Routes.CLASS_SUBJECT) { backStackEntry ->
        val className = backStackEntry.arguments?.getString("class") ?: ""
        val subject = backStackEntry.arguments?.getString("subject") ?: ""

        ClassSubjectScreen(
            className = className,
            subject = subject,
            onBack = { navController.popBackStack() }
        )
    }
}
