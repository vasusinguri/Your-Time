package com.yourtime.app.domain

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Calendar-aware age calculation engine.
 *
 * Correctly accounts for:
 * - Variable month lengths (28, 29, 30, 31 days)
 * - Leap years and February 29 birthdays
 * - End-of-month boundaries (e.g. Jan 31 -> Mar 31 is 2 months)
 * - Intra-day hour, minute, second boundaries and midnight transitions
 */
object AgeCalculator {

    /**
     * Checks whether the given birth date/time is valid (not in the future).
     */
    fun isValid(birth: LocalDateTime, current: LocalDateTime = LocalDateTime.now()): Boolean {
        return !birth.isAfter(current)
    }

    /**
     * Calculates the exact elapsed time between [birth] and [current].
     *
     * @throws IllegalArgumentException if [birth] is after [current].
     */
    fun calculate(birth: LocalDateTime, current: LocalDateTime = LocalDateTime.now()): AgeResult {
        require(!birth.isAfter(current)) {
            "Birth date and time cannot be in the future."
        }

        if (birth == current) {
            return AgeResult(
                years = 0,
                months = 0,
                days = 0,
                hours = 0,
                minutes = 0,
                seconds = 0
            )
        }

        // 1. Calculate whole years anchored from birth
        val estimatedYears = ChronoUnit.YEARS.between(birth, current)
        var years = maxOf(0L, estimatedYears - 1)
        while (!birth.plusYears(years + 1).isAfter(current)) {
            years++
        }
        val afterYears = birth.plusYears(years)

        // 2. Calculate whole months anchored from afterYears
        val estimatedMonths = ChronoUnit.MONTHS.between(afterYears, current)
        var months = maxOf(0L, estimatedMonths - 1)
        while (!afterYears.plusMonths(months + 1).isAfter(current)) {
            months++
        }
        val afterMonths = afterYears.plusMonths(months)

        // 3. Calculate whole days anchored from afterMonths
        val estimatedDays = ChronoUnit.DAYS.between(afterMonths, current)
        var days = maxOf(0L, estimatedDays - 1)
        while (!afterMonths.plusDays(days + 1).isAfter(current)) {
            days++
        }
        val afterDays = afterMonths.plusDays(days)

        // 4. Calculate intra-day hours, minutes, and seconds
        val hours = ChronoUnit.HOURS.between(afterDays, current)
        val afterHours = afterDays.plusHours(hours)

        val minutes = ChronoUnit.MINUTES.between(afterHours, current)
        val afterMinutes = afterHours.plusMinutes(minutes)

        val seconds = ChronoUnit.SECONDS.between(afterMinutes, current)

        // Total lifetime duration calculations for Journey Insights
        val totalLifetimeSeconds = java.time.Duration.between(birth, current).seconds
        val totalLifetimeDays = java.time.temporal.ChronoUnit.DAYS.between(birth.toLocalDate(), current.toLocalDate())
        val totalLifetimeHours = totalLifetimeSeconds / 3600
        val totalLifetimeMinutes = totalLifetimeSeconds / 60

        return AgeResult(
            years = years,
            months = months,
            days = days,
            hours = hours,
            minutes = minutes,
            seconds = seconds,
            totalDays = totalLifetimeDays,
            totalHours = totalLifetimeHours,
            totalMinutes = totalLifetimeMinutes,
            totalSeconds = totalLifetimeSeconds
        )
    }
}
