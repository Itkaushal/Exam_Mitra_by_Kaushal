package com.example.exammitrabykaushal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
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
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.BankPyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.GroupDPyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.JePyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.MtsChslPyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.NtpcPyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.SscCglPyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.StateExamPyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.UpscPyqScreen
import com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen.loadPdfFromFirebase
import com.google.android.gms.ads.MobileAds

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
                val startScreen = if (isUserLoggedIn) "dashboard" else "login"

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
                            onNavigateToProfile = { navController.navigate("profile") },

                            onNavigateToVideoLectures = {
                                navController.navigate("VideoLectures")
                            }
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
                            onNavigateToHistory = { navController.navigate("test_history") },
                            onNavigateToNotification = {navController.navigate("notification_screen")}
                        )
                    }

                    // PyQ Selection route
                    composable("pyq_selection") {
                        PYQSelectionScreen(
                            onExamSelected = { examName ->
                                val route = when (examName) {
                                    "UPSC CSE" -> "UPSC"
                                    "SSC CGL" -> "CGL"
                                    "SSC CHSL/MTS" -> "MTS"
                                    "SSC JE" -> "JE"
                                    "RRB NTPC" -> "NTPC"
                                    "RRB Group D" -> "GROUP-D"
                                    "State PSC" -> "StateExam"
                                    "Banking/IBPS" -> "BANK"
                                    else -> "quiz/$examName"
                                }
                                navController.navigate(route)
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // exam name route screen
                    // upsc paper
                    composable("UPSC") {
                        UpscPyqScreen(
                        examName = "UPSC",
                        /*onPaperClicked = {},*/
                        onBack = { navController.popBackStack() }
                        )
                    }

                    // ssc cgl paper
                    composable("CGL") {
                        SscCglPyqScreen(
                            examName = "CGL",
                            /*onPaperClicked = {},*/
                            onBack = {navController.popBackStack()}
                        )
                    }

                    //ssc mts paper
                    composable("MTS") {
                        MtsChslPyqScreen(
                            examName = "MTS",
                            /*onPaperClicked = {},*/
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // rrb ntpc paper
                    composable("NTPC") {
                        NtpcPyqScreen(
                            examName = "NTPC",
                            /*onPaperClicked = { paperKey ->

                            },*/
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // rrb group -d paper
                    composable("GROUP-D") {
                        GroupDPyqScreen(
                            examName = "GROUP-D",
                           /* onPaperClicked = {},*/
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // ibps bank paper
                    composable("BANK") {
                        BankPyqScreen(
                            examName = "BANK",
                            /*onPaperClicked = {},*/
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // state exam paper
                    composable("StateExam") {
                        StateExamPyqScreen(
                            examName = "StateExam",
                            /*onPaperClicked = {},*/
                            onBack = {navController.popBackStack()}
                        )
                    }

                    // ssc je exam paper
                    composable("JE") {
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