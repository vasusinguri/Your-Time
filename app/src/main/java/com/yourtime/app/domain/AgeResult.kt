package com.yourtime.app.domain

import androidx.compose.runtime.Immutable

/**
 * Represents the exact breakdown of time lived.
 */
@Immutable
data class AgeResult(
    val years: Long,
    val months: Long,
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long,
    // Total journey metrics for Insights
    val totalDays: Long = 0L,
    val totalHours: Long = 0L,
    val totalMinutes: Long = 0L,
    val totalSeconds: Long = 0L
)
