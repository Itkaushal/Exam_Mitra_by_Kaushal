package com.example.exammitrabykaushal.navigation.AppNavGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.exammitrabykaushal.UIScreens.screen.QuizScreen
import com.example.exammitrabykaushal.navigation.Routes

fun NavGraphBuilder.quizNavGraph(navController: NavHostController) {

    composable(
        route = Routes.QUIZ,
        arguments = listOf(navArgument("category") {
            type = NavType.StringType
        })
    ) {
        QuizScreen(
            category = it.arguments?.getString("category") ?: "",
            onBack = { navController.popBackStack() }
        )
    }
}
