package com.yourtime.app.domain

import androidx.compose.runtime.Immutable
import java.time.LocalDateTime
import java.util.UUID

/**
 * Represents a saved profile (e.g., Self, Family, Partner, Friend).
 */
@Immutable
data class UserProfile(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val birthDateTime: LocalDateTime,
    val tag: String = "Me"
)
