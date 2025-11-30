package com.example.exammitrabykaushal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.exammitrabykaushal.ui.theme.ExamMitraByKaushalTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.exammitrabykaushal.UIScreens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExamMitraByKaushalTheme {

                val navController = rememberNavController()
                val context = LocalContext.current

                // Check Login Status
                val isUserLoggedIn = SessionManager.isLoggedIn(context)
                val startScreen = if (isUserLoggedIn) "dashboard" else "login"

                NavHost(navController = navController, startDestination = startScreen) {

                    // Phone Auth Root
                    composable("phone_login") {
                        PhoneAuthScreen(
                            onLoginSuccess = {
                                // Navigate to Dashboard
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // --- Test History Screen ---
                    composable("test_history") {
                        TestHistoryScreen(onBack = { navController.popBackStack() })
                    }


                    // --- SIGN UP ROUTE ---
                    composable("signup") {
                        SignUpScreen(
                            onSignUpSuccess = {
                                // FIX: Navigate to LOGIN screen, NOT Dashboard
                                navController.navigate("login") {
                                    // Remove signup from backstack so they don't go back to it
                                    popUpTo("signup") { inclusive = true }
                                }
                            },
                            onNavigateToLogin = {
                                navController.popBackStack()
                            }
                        )
                    }

                    // --- LOGIN ROUTE ---
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                // Only go to Dashboard after REAL login
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToSignUp = { navController.navigate("signup") } ,
                            onNavigateToPhone = { navController.navigate("phone_login") }
                        )
                    }

                    // --- DASHBOARD ROUTE ---
                    composable("dashboard") {
                        DashboardScreen(
                            onNavigateToTest = { category ->
                                if (category == "PYQ") {
                                    navController.navigate("pyq_selection")
                                } else {
                                    navController.navigate("quiz/$category")
                                }
                            },
                            onNavigateToProfile = { navController.navigate("profile") }
                        )
                    }

                    // ... Other routes (Quiz, Profile, PYQ) remain the same ...
                    composable(
                        route = "quiz/{category}",
                        arguments = listOf(navArgument("category") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val category = backStackEntry.arguments?.getString("category") ?: "Mock"
                        QuizScreen(
                            category = category,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("profile") {
                        ProfileScreen(
                            onBack = { navController.popBackStack() },
                            onLogout = {
                                // Navigate to Login and clear all history so back button exits app
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true } // Clears everything
                                }
                            },
                            onNavigateToHistory = { navController.navigate("test_history") }
                        )
                    }

                    composable("pyq_selection") {
                        PYQSelectionScreen(
                            onExamSelected = { examName ->
                                navController.navigate("quiz/$examName PYQ")
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}