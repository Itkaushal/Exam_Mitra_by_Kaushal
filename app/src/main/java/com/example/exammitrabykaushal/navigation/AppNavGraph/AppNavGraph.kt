package com.example.exammitrabykaushal.navigation.AppNavGraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.exammitrabykaushal.navigation.Routes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController) {

    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        authNavGraph(navController, context)
        mainNavGraph(navController)
        pyqNavGraph(navController)
        videoNavGraph(navController)
        quizNavGraph(navController)
        notificationNavGraph(navController)
        createTestNavGraph(navController)

    }
}
