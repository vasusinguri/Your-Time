package com.yourtime.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "your_time_preferences")

/**
 * Manages local, offline persistence of the user's birth date and time
 * using Android Jetpack DataStore Preferences.
 */
class PreferencesManager(private val context: Context) {

    private object PreferenceKeys {
        val BIRTH_YEAR = intPreferencesKey("birth_year")
        val BIRTH_MONTH = intPreferencesKey("birth_month")
        val BIRTH_DAY = intPreferencesKey("birth_day")
        val BIRTH_HOUR = intPreferencesKey("birth_hour")
        val BIRTH_MINUTE = intPreferencesKey("birth_minute")
        val BIRTH_SECOND = intPreferencesKey("birth_second")
    }

    /**
     * Emits the saved birth [LocalDateTime], or null if none is saved.
     */
    val birthDateTimeFlow: Flow<LocalDateTime?> = context.dataStore.data.map { preferences ->
        val year = preferences[PreferenceKeys.BIRTH_YEAR]
        val month = preferences[PreferenceKeys.BIRTH_MONTH]
        val day = preferences[PreferenceKeys.BIRTH_DAY]
        val hour = preferences[PreferenceKeys.BIRTH_HOUR] ?: 0
        val minute = preferences[PreferenceKeys.BIRTH_MINUTE] ?: 0
        val second = preferences[PreferenceKeys.BIRTH_SECOND] ?: 0

        if (year != null && month != null && day != null) {
            try {
                LocalDateTime.of(year, month, day, hour, minute, second)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    /**
     * Saves the birth date and time to DataStore Preferences.
     */
    suspend fun saveBirthDateTime(dateTime: LocalDateTime) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.BIRTH_YEAR] = dateTime.year
            preferences[PreferenceKeys.BIRTH_MONTH] = dateTime.monthValue
            preferences[PreferenceKeys.BIRTH_DAY] = dateTime.dayOfMonth
            preferences[PreferenceKeys.BIRTH_HOUR] = dateTime.hour
            preferences[PreferenceKeys.BIRTH_MINUTE] = dateTime.minute
            preferences[PreferenceKeys.BIRTH_SECOND] = dateTime.second
        }
    }

    /**
     * Clears all saved birth data from local storage.
     */
    suspend fun clearBirthDateTime() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferenceKeys.BIRTH_YEAR)
            preferences.remove(PreferenceKeys.BIRTH_MONTH)
            preferences.remove(PreferenceKeys.BIRTH_DAY)
            preferences.remove(PreferenceKeys.BIRTH_HOUR)
            preferences.remove(PreferenceKeys.BIRTH_MINUTE)
            preferences.remove(PreferenceKeys.BIRTH_SECOND)
        }
    }
}
