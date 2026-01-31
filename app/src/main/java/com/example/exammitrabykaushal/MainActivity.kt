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
import com.example.exammitrabykaushal.navigation.AppNavGraph.AppNavGraph
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
                AppNavGraph(navController)
                }
            }
        }
    }