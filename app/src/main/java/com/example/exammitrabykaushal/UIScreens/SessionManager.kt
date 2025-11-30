package com.example.exammitrabykaushal.UIScreens


import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "ExamMitraPref"
    private const val IS_LOGGED_IN = "IsLoggedIn"

    private const val USER_NAME = "UserName" // New Key

    // Save Login State
    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    // Check Login State
    fun isLoggedIn(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(IS_LOGGED_IN, false) // Default is false (not logged in)
    }

    fun saveUserName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(USER_NAME, name).apply()
    }

    fun getUserName(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(USER_NAME, "Aspirant") ?: "Aspirant"
    }
}