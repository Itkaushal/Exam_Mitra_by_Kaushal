package com.example.exammitrabykaushal.navigation.AppNavGraph

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.exammitrabykaushal.UIScreens.auth.LoginScreen
import com.example.exammitrabykaushal.UIScreens.auth.PhoneAuthScreen
import com.example.exammitrabykaushal.UIScreens.auth.SignUpScreen
import com.example.exammitrabykaushal.UIScreens.session.SessionManager
import com.example.exammitrabykaushal.UIScreens.splash.ExamMitraSplashScreen
import com.example.exammitrabykaushal.navigation.Routes

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    context: Context
) {

    composable(Routes.SPLASH) {
        ExamMitraSplashScreen {
            if (SessionManager.isLoggedIn(context)) {
                navController.navigate(Routes.DASHBOARD) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            } else {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
        }
    }

    composable(Routes.LOGIN) {
        LoginScreen(
            onLoginSuccess = {
                navController.navigate(Routes.DASHBOARD) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            },
            onNavigateToSignUp = { navController.navigate(Routes.SIGNUP) },
            onNavigateToPhone = { navController.navigate(Routes.PHONE_LOGIN) }
        )
    }

    composable(Routes.SIGNUP) {
        SignUpScreen(
            onSignUpSuccess = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SIGNUP) { inclusive = true }
                }
            },
            onNavigateToLogin = { navController.popBackStack() }
        )
    }

    composable(Routes.PHONE_LOGIN) {
        PhoneAuthScreen(
            onLoginSuccess = {
                navController.navigate(Routes.DASHBOARD) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            },
            onBack = { navController.popBackStack() }
        )
    }
}
