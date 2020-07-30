package de.jenswangenheim.cleanarchitecture

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class SharedPrefsHelper {
    companion object {
        private const val PREF_LAST_UPDATE_TIME = "pref_last_update_time"
        private var prefs: SharedPreferences? = null
        @Volatile private var instance: SharedPrefsHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPrefsHelper = instance ?: synchronized(LOCK) {
            instance ?: buildPrefsHelper(context).also {
                instance = it
            }
        }

        private fun buildPrefsHelper(context: Context): SharedPrefsHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPrefsHelper()
        }
    }

    fun saveLastUpdateTime(time: Long) {
        prefs?.edit(commit = true) {
            putLong(PREF_LAST_UPDATE_TIME, time)
        }
    }

    fun getLastUpdateTime() = prefs?.getLong(PREF_LAST_UPDATE_TIME, 0L)
}