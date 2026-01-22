package com.example.exammitrabykaushal.UIScreens.session

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDailyStreak(context: Context): Int {
        val prefs = context.getSharedPreferences("streak_prefs", Context.MODE_PRIVATE)
        val lastDate = prefs.getString("last_date", null)
        val today = java.time.LocalDate.now().toString()

        var streak = prefs.getInt("streak", 0)

        if (lastDate == null) {
            streak = 1
        } else {
            val diff = java.time.temporal.ChronoUnit.DAYS.between(
                java.time.LocalDate.parse(lastDate),
                java.time.LocalDate.parse(today)
            )

            streak = when {
                diff == 0L -> streak
                diff == 1L -> streak + 1
                else -> 1
            }
        }

        prefs.edit()
            .putString("last_date", today)
            .putInt("streak", streak)
            .apply()

        return streak
    }

}

