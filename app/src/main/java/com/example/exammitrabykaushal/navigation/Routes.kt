package com.example.exammitrabykaushal.navigation

object Routes {

    // Auth
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val PHONE_LOGIN = "phone_login"

    // Main
    const val DASHBOARD = "dashboard"
    const val PROFILE = "profile"
    const val NOTIFICATION = "notification_screen"
    const val TEST_HISTORY = "test_history"

    // Quiz
    const val QUIZ = "quiz/{category}"
    fun quiz(category: String) = "quiz/$category"

    // Create Test
    const val CREATE_TEST_HOME = "create_test_home"
    const val TEST_CONFIG = "test_config/{category}"
    fun testConfig(category: String) = "test_config/$category"

    // Test (Create Test Flow)
    const val TEST_SCREEN = "test_screen/{category}/{questions}/{time}"
    fun testScreen(
        category: String,
        questions: Int,
        time: Int
    ) = "test_screen/$category/$questions/$time"



    // PYQ
    const val PYQ_SELECTION = "pyq_selection"
    const val UPSC = "pyq_upsc"
    const val CGL = "pyq_cgl"
    const val MTS = "pyq_mts"
    const val JE = "pyq_je"
    const val NTPC = "pyq_ntpc"
    const val GROUP_D = "pyq_group_d"
    const val STATE = "pyq_state"
    const val BANK = "pyq_bank"

    // Videos
    const val VIDEO_HOME = "video_home"
    const val MATH = "video_math"
    const val REASONING = "video_reasoning"
    const val GK = "video_gk"
    const val ENGLISH = "video_english"
    const val HINDI = "video_hindi"
    const val SCIENCE = "video_science"
    const val COMPUTER = "video_computer"
    const val CA = "video_ca"

    /*// 9–12 Section
    const val NINE_TO_TWELVE_HOME = "nine_to_twelve_home"
    const val CLASS_9 = "class_9"
    const val CLASS_10 = "class_10"
    const val CLASS_11 = "class_11"
    const val CLASS_12 = "class_12"

    const val CLASS_SUBJECT = "class_subject/{class}/{subject}"
    fun classSubject(className: String, subject: String) =
        "class_subject/$className/$subject"*/

}
