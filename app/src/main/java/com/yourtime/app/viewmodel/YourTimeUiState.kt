package com.yourtime.app.viewmodel

import androidx.compose.runtime.Immutable
import com.yourtime.app.domain.AgeResult
import com.yourtime.app.domain.NextBirthdayResult
import com.yourtime.app.domain.UserProfile
import java.time.LocalDate
import java.time.LocalTime

/**
 * UI State representing the state of Your Time app.
 */
@Immutable
sealed interface YourTimeUiState {

    /**
     * Initial/Empty state: When no profile has been created yet.
     */
    @Immutable
    data class Empty(
        val initialName: String = "Me",
        val selectedDate: LocalDate? = null,
        val selectedTime: LocalTime = LocalTime.MIDNIGHT,
        val errorMessage: String? = null
    ) : YourTimeUiState

    /**
     * Ready state: Live ticking dashboard with active profile and next birthday countdown.
     */
    @Immutable
    data class Ready(
        val profiles: List<UserProfile>,
        val activeProfileId: String,
        val activeProfile: UserProfile,
        val age: AgeResult,
        val nextBirthday: NextBirthdayResult,
        val isAddingProfile: Boolean = false,
        val editingProfile: UserProfile? = null
    ) : YourTimeUiState
}
