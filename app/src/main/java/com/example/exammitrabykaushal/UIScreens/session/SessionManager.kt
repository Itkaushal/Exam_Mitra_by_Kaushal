package com.example.exammitrabykaushal.UIScreens.session

import android.content.Context
import android.content.SharedPreferences
import com.example.exammitrabykaushal.UIScreens.session.SessionManager.PREF_NAME

object SessionManager {

    private const val PREF_NAME = "user_session"
    private const val KEY_NAME = "user_name"
    private const val KEY_PHOTO = "user_photo"
    private const val KEY_LOGIN = "is_logged_in"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveUserName(context: Context, name: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_NAME, name)
            .apply()
    }

    fun saveUserPhoto(context: Context, photo: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_PHOTO, photo)
            .apply()
    }

    fun getUserName(context: Context): String =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_NAME, "Guest User") ?: "Guest User"

    fun getUserPhoto(context: Context): String =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_PHOTO, "") ?: ""

    fun setLoggedIn(context: Context, loggedIn: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_LOGIN, loggedIn)
            .apply()
    }

    /** ✅ THIS WAS MISSING */
    fun isLoggedIn(context: Context): Boolean =
        prefs(context).getBoolean(KEY_LOGIN, false)

    fun clearSession(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}

