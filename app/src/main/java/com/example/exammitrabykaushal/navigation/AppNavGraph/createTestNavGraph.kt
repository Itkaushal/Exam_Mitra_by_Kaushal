package com.example.exammitrabykaushal.navigation.AppNavGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.exammitrabykaushal.UIScreens.screen.testscreen.CreateTestHomeScreen
import com.example.exammitrabykaushal.UIScreens.screen.testscreen.ReviewAndResultForTestScreen
import com.example.exammitrabykaushal.UIScreens.screen.testscreen.TestConfigScreen
import com.example.exammitrabykaushal.UIScreens.screen.testscreen.TestScreen
import com.example.exammitrabykaushal.navigation.Routes

fun NavGraphBuilder.createTestNavGraph(
    navController: NavHostController
) {

    composable(Routes.CREATE_TEST_HOME) {
        CreateTestHomeScreen(
            onSubjectSelected = { category ->
                navController.navigate(Routes.testConfig(category))
            },
            onBack = { navController.popBackStack() }
        )
    }

    composable(Routes.TEST_CONFIG) { backStackEntry ->
        val category =
            backStackEntry.arguments?.getString("category") ?: "math"

        TestConfigScreen(
            category = category,
            onStartTest = { cat, questions, time ->
                navController.navigate(
                    Routes.testScreen(cat, questions, time)
                )
            },
            onBack = { navController.popBackStack() }
        )
    }

    composable(Routes.TEST_SCREEN) { backStackEntry ->

        val category =
            backStackEntry.arguments?.getString("category") ?: "math"
        val questions =
            backStackEntry.arguments?.getString("questions")?.toInt() ?: 10
        val time =
            backStackEntry.arguments?.getString("time")?.toInt() ?: 10

        TestScreen(
            category = category,
            totalQuestions = questions,
            onSubmit = { score, attempted, unAttempted ->
                navController.navigate(
                    "test_result/$score/$attempted/$unAttempted"
                )
            },
            onBack = { navController.popBackStack() }
        )
    }

    composable(
        route = "test_result/{score}/{attempted}/{unAttempted}",
        arguments = listOf(
            navArgument("score") { type = NavType.IntType },
            navArgument("attempted") { type = NavType.IntType },
            navArgument("unAttempted") { type = NavType.IntType }
        )
    ) { backStackEntry ->

        ReviewAndResultForTestScreen(
            score = backStackEntry.arguments?.getInt("score") ?: 0,
            attempted = backStackEntry.arguments?.getInt("attempted") ?: 0,
            unAttempted = backStackEntry.arguments?.getInt("unAttempted") ?: 0,
            onBackToDashboard = {
                navController.popBackStack(Routes.CREATE_TEST_HOME, false)
            },
            onRetry = {
                navController.popBackStack(Routes.TEST_SCREEN, inclusive = false)
            }
        )
    }
}

