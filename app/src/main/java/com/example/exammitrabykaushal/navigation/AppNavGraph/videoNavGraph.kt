package com.example.exammitrabykaushal.navigation.AppNavGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.ComputerPlaylistScreen
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.CurrentAffairsPlaylistScreen
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.EnglishPlaylistScreen
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.GkGsPlaylistScreen
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.HindiPlaylistScreen
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.MathPlaylistScreen
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.ReasoningPlaylistScreen
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.SciencePlaylistScreen
import com.example.exammitrabykaushal.UIScreens.screen.FreeVideoLecturesScreen
import com.example.exammitrabykaushal.navigation.Routes

fun NavGraphBuilder.videoNavGraph(navController: NavHostController) {

    composable(Routes.VIDEO_HOME) {
        FreeVideoLecturesScreen(
            onSubjectSelected = {
                navController.navigate(
                    when (it) {
                        "Maths" -> Routes.MATH
                        "Reasoning" -> Routes.REASONING
                        "GK/GS" -> Routes.GK
                        "English" -> Routes.ENGLISH
                        "Hindi" -> Routes.HINDI
                        "Science" -> Routes.SCIENCE
                        "Computer" -> Routes.COMPUTER
                        else -> Routes.CA
                    }
                )
            },
            onBack = { navController.popBackStack() }
        )
    }

    composable(Routes.MATH) { MathPlaylistScreen({}, { navController.popBackStack() }) }
    composable(Routes.REASONING) { ReasoningPlaylistScreen({}, { navController.popBackStack() }) }
    composable(Routes.GK) { GkGsPlaylistScreen({}, { navController.popBackStack() }) }
    composable(Routes.ENGLISH) { EnglishPlaylistScreen({}, { navController.popBackStack() }) }
    composable(Routes.HINDI) { HindiPlaylistScreen({}, { navController.popBackStack() }) }
    composable(Routes.SCIENCE) { SciencePlaylistScreen({}, { navController.popBackStack() }) }
    composable(Routes.COMPUTER) { ComputerPlaylistScreen({}, { navController.popBackStack() }) }
    composable(Routes.CA) { CurrentAffairsPlaylistScreen({}, { navController.popBackStack() }) }
}
