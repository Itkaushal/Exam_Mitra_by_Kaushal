package com.example.exammitrabykaushal.UIScreens


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "ExamMitraPref"
    private const val IS_LOGGED_IN = "IsLoggedIn"

    // Save Login State
    @SuppressLint("UseKtx")
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


    // -----------------
    @SuppressLint("UseKtx")
    fun saveUserName(context: Context, name: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putString("user_name", name).apply()
    }

    fun getUserName(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString("user_name", "Guest User") ?: "Guest User"
    }

    @SuppressLint("UseKtx")
    fun saveUserPhoto(context: Context, url: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putString("user_photo", url).apply()
    }

    fun getUserPhoto(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString("user_photo", null)
    }
}