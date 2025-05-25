package com.example.projet_session3.helper


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import android.app.Activity





///


class PrefHelper(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "app_preferences"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    // --- DARK MODE ---
    fun isDarkModeEnabled(): Boolean {
        return prefs.getBoolean(KEY_DARK_MODE, false)
    }

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()

        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Force l'activité à se recréer pour appliquer le thème
        val activity = context as? Activity
        activity?.recreate()
    }

    // --- LANGUAGE ---
    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, "fr") ?: "fr"
    }

    fun setLanguage(langCode: String) {
        prefs.edit().putString(KEY_LANGUAGE, langCode).apply()
    }

    // --- LOGIN STATE ---
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }
}
