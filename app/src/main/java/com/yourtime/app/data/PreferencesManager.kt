package com.yourtime.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yourtime.app.domain.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "your_time_preferences")

class PreferencesManager(private val context: Context) {

    private object Keys {
        val PROFILES_JSON = stringPreferencesKey("profiles_json")
        val ACTIVE_PROFILE_ID = stringPreferencesKey("active_profile_id")

        // Legacy keys for automatic migration
        val LEGACY_BIRTH_YEAR = intPreferencesKey("birth_year")
        val LEGACY_BIRTH_MONTH = intPreferencesKey("birth_month")
        val LEGACY_BIRTH_DAY = intPreferencesKey("birth_day")
        val LEGACY_BIRTH_HOUR = intPreferencesKey("birth_hour")
        val LEGACY_BIRTH_MINUTE = intPreferencesKey("birth_minute")
        val LEGACY_BIRTH_SECOND = intPreferencesKey("birth_second")
    }

    private val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    val profilesFlow: Flow<List<UserProfile>> = context.dataStore.data.map { preferences ->
        val jsonStr = preferences[Keys.PROFILES_JSON]
        if (!jsonStr.isNullOrBlank()) {
            deserializeProfiles(jsonStr)
        } else {
            // Check legacy birth date and auto-migrate
            val legacyYear = preferences[Keys.LEGACY_BIRTH_YEAR]
            val legacyMonth = preferences[Keys.LEGACY_BIRTH_MONTH]
            val legacyDay = preferences[Keys.LEGACY_BIRTH_DAY]
            if (legacyYear != null && legacyMonth != null && legacyDay != null) {
                val hour = preferences[Keys.LEGACY_BIRTH_HOUR] ?: 0
                val minute = preferences[Keys.LEGACY_BIRTH_MINUTE] ?: 0
                val second = preferences[Keys.LEGACY_BIRTH_SECOND] ?: 0
                val legacyDateTime = LocalDateTime.of(legacyYear, legacyMonth, legacyDay, hour, minute, second)
                listOf(
                    UserProfile(
                        id = "default_me",
                        name = "Me",
                        birthDateTime = legacyDateTime,
                        tag = "Me"
                    )
                )
            } else {
                emptyList()
            }
        }
    }

    val activeProfileIdFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[Keys.ACTIVE_PROFILE_ID] ?: "default_me"
    }

    suspend fun saveProfiles(profiles: List<UserProfile>) {
        context.dataStore.edit { preferences ->
            preferences[Keys.PROFILES_JSON] = serializeProfiles(profiles)
        }
    }

    suspend fun setActiveProfileId(profileId: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.ACTIVE_PROFILE_ID] = profileId
        }
    }

    suspend fun addOrUpdateProfile(profile: UserProfile) {
        context.dataStore.edit { preferences ->
            val currentList = deserializeProfiles(preferences[Keys.PROFILES_JSON] ?: "").toMutableList()
            val index = currentList.indexOfFirst { it.id == profile.id }
            if (index >= 0) {
                currentList[index] = profile
            } else {
                currentList.add(profile)
            }
            preferences[Keys.PROFILES_JSON] = serializeProfiles(currentList)
            preferences[Keys.ACTIVE_PROFILE_ID] = profile.id
        }
    }

    suspend fun deleteProfile(profileId: String) {
        context.dataStore.edit { preferences ->
            val currentList = deserializeProfiles(preferences[Keys.PROFILES_JSON] ?: "").toMutableList()
            currentList.removeAll { it.id == profileId }
            preferences[Keys.PROFILES_JSON] = serializeProfiles(currentList)
            if (preferences[Keys.ACTIVE_PROFILE_ID] == profileId) {
                preferences[Keys.ACTIVE_PROFILE_ID] = currentList.firstOrNull()?.id ?: ""
            }
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private fun serializeProfiles(profiles: List<UserProfile>): String {
        val array = JSONArray()
        profiles.forEach { p ->
            val obj = JSONObject()
            obj.put("id", p.id)
            obj.put("name", p.name)
            obj.put("birth", p.birthDateTime.format(isoFormatter))
            obj.put("tag", p.tag)
            array.put(obj)
        }
        return array.toString()
    }

    private fun deserializeProfiles(jsonStr: String): List<UserProfile> {
        if (jsonStr.isBlank()) return emptyList()
        val list = mutableListOf<UserProfile>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    UserProfile(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        name = obj.optString("name", "Profile"),
                        birthDateTime = LocalDateTime.parse(obj.getString("birth"), isoFormatter),
                        tag = obj.optString("tag", "Me")
                    )
                )
            }
        } catch (_: Exception) {}
        return list
    }
}
