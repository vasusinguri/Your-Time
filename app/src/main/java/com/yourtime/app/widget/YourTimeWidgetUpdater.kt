package com.yourtime.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.yourtime.app.MainActivity
import com.yourtime.app.R
import com.yourtime.app.data.PreferencesManager
import com.yourtime.app.domain.AgeCalculator
import com.yourtime.app.domain.NextBirthdayCalculator
import com.yourtime.app.domain.PlanetaryAgeCalculator
import com.yourtime.app.domain.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Locale

object YourTimeWidgetUpdater {

    private val scope = CoroutineScope(Dispatchers.IO)

    fun updateAllWidgets(context: Context) {
        scope.launch {
            val appWidgetManager = AppWidgetManager.getInstance(context) ?: return@launch

            val ageWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, AgeWidgetProvider::class.java)
            )
            if (ageWidgetIds.isNotEmpty()) {
                updateAgeWidgetsInternal(context, appWidgetManager, ageWidgetIds)
            }

            val planetaryWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, PlanetaryWidgetProvider::class.java)
            )
            if (planetaryWidgetIds.isNotEmpty()) {
                updatePlanetaryWidgetsInternal(context, appWidgetManager, planetaryWidgetIds)
            }
        }
    }

    suspend fun updateAgeWidgetsInternal(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val activeProfile = getActiveProfile(context)
        val now = LocalDateTime.now()

        for (widgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_age_card)

            // Click pending intent to launch main activity
            val launchIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                widgetId,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_age_root, pendingIntent)

            if (activeProfile != null) {
                val age = AgeCalculator.calculate(activeProfile.birthDateTime, now)
                val birthday = NextBirthdayCalculator.calculate(activeProfile.birthDateTime, now)

                views.setTextViewText(R.id.widget_profile_name, "• ${activeProfile.name}")
                views.setTextViewText(R.id.widget_years_value, "${age.years}")
                views.setTextViewText(R.id.widget_months_value, "${age.months}")
                views.setTextViewText(R.id.widget_days_value, "${age.days}")

                val birthdayText = if (birthday.isToday) {
                    "🎉 Happy Birthday Today!"
                } else {
                    "🎂 Next Birthday in ${birthday.days} days"
                }
                views.setTextViewText(R.id.widget_birthday_text, birthdayText)
            } else {
                views.setTextViewText(R.id.widget_profile_name, "• Welcome")
                views.setTextViewText(R.id.widget_years_value, "--")
                views.setTextViewText(R.id.widget_months_value, "--")
                views.setTextViewText(R.id.widget_days_value, "--")
                views.setTextViewText(R.id.widget_birthday_text, "Tap to configure Your Time")
            }

            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }

    suspend fun updatePlanetaryWidgetsInternal(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val activeProfile = getActiveProfile(context)
        val now = LocalDateTime.now()

        for (widgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_planetary_card)

            val launchIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                widgetId,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_planetary_root, pendingIntent)

            if (activeProfile != null) {
                val planets = PlanetaryAgeCalculator.calculate(activeProfile.birthDateTime, now)
                val earthAge = planets.firstOrNull { it.planetName == "Earth" }?.ageInYears ?: 0.0
                val mercuryAge = planets.firstOrNull { it.planetName == "Mercury" }?.ageInYears ?: 0.0
                val marsAge = planets.firstOrNull { it.planetName == "Mars" }?.ageInYears ?: 0.0
                val jupiterAge = planets.firstOrNull { it.planetName == "Jupiter" }?.ageInYears ?: 0.0

                views.setTextViewText(R.id.widget_planetary_profile_name, "• ${activeProfile.name}")
                views.setTextViewText(R.id.widget_planet_earth_val, String.format(Locale.US, "%.1f y", earthAge))
                views.setTextViewText(R.id.widget_planet_mercury_val, String.format(Locale.US, "%.1f y", mercuryAge))
                views.setTextViewText(R.id.widget_planet_mars_val, String.format(Locale.US, "%.1f y", marsAge))
                views.setTextViewText(R.id.widget_planet_jupiter_val, String.format(Locale.US, "%.1f y", jupiterAge))
            } else {
                views.setTextViewText(R.id.widget_planetary_profile_name, "• Welcome")
                views.setTextViewText(R.id.widget_planet_earth_val, "--")
                views.setTextViewText(R.id.widget_planet_mercury_val, "--")
                views.setTextViewText(R.id.widget_planet_mars_val, "--")
                views.setTextViewText(R.id.widget_planet_jupiter_val, "--")
            }

            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }

    private suspend fun getActiveProfile(context: Context): UserProfile? {
        val prefs = PreferencesManager(context)
        val profiles = prefs.profilesFlow.firstOrNull() ?: emptyList()
        val activeId = prefs.activeProfileIdFlow.firstOrNull()
        return profiles.firstOrNull { it.id == activeId } ?: profiles.firstOrNull()
    }
}
