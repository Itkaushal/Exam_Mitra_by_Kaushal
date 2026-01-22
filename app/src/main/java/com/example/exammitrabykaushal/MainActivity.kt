package com.example.exammitrabykaushal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.exammitrabykaushal.ui.theme.ExamMitraByKaushalTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.exammitrabykaushal.UIScreens.*
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.ComputerPlaylistScreen
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.CurrentAffairsPlaylistScreen
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.EnglishPlaylistScreen
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.GkGsPlaylistScreen
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.HindiPlaylistScreen
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.MathPlaylistScreen
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.ReasoningPlaylistScreen
import com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen.SciencePlaylistScreen
import com.example.exammitrabykaushal.UIScreens.auth.LoginScreen
import com.example.exammitrabykaushal.UIScreens.auth.PhoneAuthScreen
import com.example.exammitrabykaushal.UIScreens.auth.SignUpScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.BankPyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.GroupDPyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.JePyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.MtsChslPyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.NtpcPyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.SscCglPyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.StateExamPyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.UpscPyqScreen
import com.example.exammitrabykaushal.UIScreens.screen.DashboardScreen
import com.example.exammitrabykaushal.UIScreens.screen.FreeVideoLecturesScreen
import com.example.exammitrabykaushal.UIScreens.screen.NotificationScreen
import com.example.exammitrabykaushal.UIScreens.screen.PYQSelectionScreen
import com.example.exammitrabykaushal.UIScreens.screen.ProfileScreen
import com.example.exammitrabykaushal.UIScreens.screen.QuizScreen
import com.example.exammitrabykaushal.UIScreens.screen.TestHistoryScreen
import com.example.exammitrabykaushal.UIScreens.session.SessionManager
import com.example.exammitrabykaushal.UIScreens.splash.ExamMitraSplashScreen
import com.example.exammitrabykaushal.navigation.Routes
import com.google.android.gms.ads.MobileAds
import okhttp3.Route

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MobileAds.initialize(this) // initialize mobile ads

        setContent {
            ExamMitraByKaushalTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                // Check Login Status
                val isUserLoggedIn = SessionManager.isLoggedIn(context)

                NavHost(navController = navController, startDestination = "splash") {
                    // --- Splash Screen Route ---
                    composable("splash") {
                        ExamMitraSplashScreen {
                            val isLoggedIn = SessionManager.isLoggedIn(context)
                            if (isLoggedIn) {
                                navController.navigate("dashboard") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            } else {
                                navController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        }
                    }
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

                    // --- Notification Screen
                    composable("notification_screen") {
                        NotificationScreen(onBack = { navController.popBackStack() })
                    }
                    // --- SIGN UP ROUTE ---
                    composable("signup") {
                        SignUpScreen(
                            onSignUpSuccess = {
                                navController.navigate("login") {
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
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToSignUp = { navController.navigate("signup") },
                            onNavigateToPhone = { navController.navigate("phone_login") }
                        )
                    }

                    // --- DASHBOARD ROUTE ---
                    composable("dashboard") {
                        DashboardScreen(
                            onNavigateToTest = { testType ->
                                when (testType) {
                                    "pyq" -> navController.navigate(Routes.PYQ_SELECTION)
                                    "math" -> navController.navigate("quiz/math")
                                    "reasoning" -> navController.navigate("quiz/reasoning")
                                    "gs" -> navController.navigate("quiz/gs")
                                    "full_length" -> navController.navigate("quiz/full_length")
                                }
                            },

                            onNavigateToProfile = { navController.navigate("profile") },
                            onNavigateToVideoLectures = {
                                navController.navigate("VideoLectures")
                            }
                        )
                    }

                    //Quiz.
                    composable(
                        route = "quiz/{category}",
                        arguments = listOf(navArgument("category") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val category = backStackEntry.arguments?.getString("category") ?: "rrb_je_2024"
                        QuizScreen(
                            category = category,
                            onBack = { navController.popBackStack() },
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
                            onNavigateToHistory = { navController.navigate("test_history") },
                            onNavigateToNotification = { navController.navigate("notification_screen") }
                        )
                    }

                    // PyQ Selection route
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
                                route?.let {
                                    navController.navigate(it)
                                }

                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // upsc paper
                    composable(Routes.UPSC) {
                        UpscPyqScreen(
                        examName = "UPSC",
                        /*onPaperClicked = {},*/
                        onBack = { navController.popBackStack() }
                        )
                    }

                    // ssc cgl paper
                    composable(Routes.CGL) {
                        SscCglPyqScreen(
                            examName = "CGL",
                            /*onPaperClicked = {},*/
                            onBack = {navController.popBackStack()}
                        )
                    }

                    //ssc mts paper
                    composable(Routes.MTS) {
                        MtsChslPyqScreen(
                            examName = "MTS",
                            /*onPaperClicked = {},*/
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // rrb ntpc paper
                    composable(Routes.NTPC) {
                        NtpcPyqScreen(
                            examName = "NTPC",
                            /*onPaperClicked = { paperKey ->

                            },*/
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // rrb group -d paper
                    composable(Routes.GROUP_D) {
                        GroupDPyqScreen(
                            examName = "GROUP-D",
                           /* onPaperClicked = {},*/
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // ibps bank paper
                    composable(Routes.BANK) {
                        BankPyqScreen(
                            examName = "BANK",
                            /*onPaperClicked = {},*/
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // state exam paper
                    composable(Routes.STATE) {
                        StateExamPyqScreen(
                            examName = "StateExam",
                            /*onPaperClicked = {},*/
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // ssc je exam paper
                    composable(Routes.JE) {
                        JePyqScreen(
                            examName = "JE",
                           /* onPaperClicked = {},*/
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // video lectures screen route------
                    composable("VideoLectures") {
                        FreeVideoLecturesScreen(
                            onSubjectSelected = { subjectName ->
                                val route = when (subjectName) {
                                    "Maths" -> "Math"
                                    "Reasoning" -> "Reasoning"
                                    "GK/GS" -> "GK"
                                    "English" -> "English"
                                    "Hindi" -> "Hindi"
                                    "Science" -> "Science"
                                    "Computer" -> "Computer"
                                    "Current Affairs" -> "CA"
                                    else -> "quiz/$subjectName"
                                }
                                navController.navigate(route)
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // video subject wise screen

                    // math video plalist
                    composable("Math") {
                        MathPlaylistScreen(
                            onVideoSelected = { videoName ->
                                // naviagte ,.....
                            },
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // reasoning video plalist
                    composable("Reasoning") {
                        ReasoningPlaylistScreen(
                            onVideoSelected = { videoName ->
                                // navigate -----
                            },
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // gk/gs video playlist
                    composable("GK") {
                        GkGsPlaylistScreen(
                            onVideoSelected = { videoName ->
                                // navigate -----
                            },
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // english video playlist
                    composable("English") {
                        EnglishPlaylistScreen(
                            onVideoSelected = { videoName ->
                                // navigate -----
                            },
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // hindi video playlist
                    composable("Hindi") {
                        HindiPlaylistScreen(
                            onVideoSelected = { videoName ->
                                // navigate -----
                            },
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // science video playlist
                    composable("Science") {
                        SciencePlaylistScreen(
                            onVideoSelected = { videoName ->
                                // navigate -----
                            },
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // computer video playlist
                    composable("Computer") {
                        ComputerPlaylistScreen(
                            onVideoSelected = { videoName ->
                                // navigate -----
                            },
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // current affair's video playlist
                    composable("CA") {
                        CurrentAffairsPlaylistScreen(
                            onVideoSelected = { videoName ->
                                // navigate -----
                            },
                            onBack = {navController.popBackStack()}
                        )
                    }

                }
            }
        }
    }
}