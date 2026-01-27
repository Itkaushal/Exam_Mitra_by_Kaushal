package com.example.exammitrabykaushal.navigation.AppNavGraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.*
import com.example.exammitrabykaushal.UIScreens.screen.PYQSelectionScreen
import com.example.exammitrabykaushal.navigation.Routes

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.pyqNavGraph(
    navController: NavHostController
) {

    // PYQ Selection Screen
    composable(Routes.PYQ_SELECTION) {
        PYQSelectionScreen(
            onExamSelected = { examName ->
                val route = when (examName) {
                    "UPSC CSE" -> Routes.UPSC
                    "SSC CGL" -> Routes.CGL
                    "SSC CHSL/MTS" -> Routes.MTS
                    "SSC JE" -> Routes.JE
                    "RRB NTPC" -> Routes.NTPC
                    "RRB Group D" -> Routes.GROUP_D
                    "State PSC" -> Routes.STATE
                    "Banking/IBPS" -> Routes.BANK
                    else -> null
                }

                route?.let { navController.navigate(it) }
            },
            onBack = { navController.popBackStack() }
        )
    }

    // UPSC
    composable(Routes.UPSC) {
        UpscPyqScreen(
            examName = "UPSC",
            onBack = { navController.popBackStack() }
        )
    }

    // SSC CGL
    composable(Routes.CGL) {
        SscCglPyqScreen(
            examName = "CGL",
            onBack = { navController.popBackStack() }
        )
    }

    // SSC CHSL / MTS
    composable(Routes.MTS) {
        MtsChslPyqScreen(
            examName = "MTS",
            onBack = { navController.popBackStack() }
        )
    }

    // SSC JE
    composable(Routes.JE) {
        JePyqScreen(
            examName = "JE",
            onBack = { navController.popBackStack() }
        )
    }

    // RRB NTPC
    composable(Routes.NTPC) {
        NtpcPyqScreen(
            examName = "NTPC",
            onBack = { navController.popBackStack() }
        )
    }

    // RRB Group D
    composable(Routes.GROUP_D) {
        GroupDPyqScreen(
            examName = "GROUP-D",
            onBack = { navController.popBackStack() }
        )
    }

    // State PSC
    composable(Routes.STATE) {
        StateExamPyqScreen(
            examName = "StateExam",
            onBack = { navController.popBackStack() }
        )
    }

    // Banking / IBPS
    composable(Routes.BANK) {
        BankPyqScreen(
            examName = "BANK",
            onBack = { navController.popBackStack() }
        )
    }
}
