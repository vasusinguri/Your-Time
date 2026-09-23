package com.yourtime.app.viewmodel

import com.yourtime.app.domain.AgeResult
import com.yourtime.app.domain.NextBirthdayResult
import com.yourtime.app.domain.PlanetaryAge
import com.yourtime.app.domain.UserProfile
import java.time.LocalDate
import java.time.LocalTime

/**
 * UI State representing the state of Your Time app.
 */
sealed interface YourTimeUiState {

    /**
     * Initial/Empty state: When no profile has been created yet.
     */
    data class Empty(
        val initialName: String = "Me",
        val selectedDate: LocalDate? = null,
        val selectedTime: LocalTime = LocalTime.MIDNIGHT,
        val errorMessage: String? = null
    ) : YourTimeUiState

    /**
     * Ready state: Live ticking dashboard with active profile, next birthday, and planetary ages.
     */
    data class Ready(
        val profiles: List<UserProfile>,
        val activeProfileId: String,
        val activeProfile: UserProfile,
        val age: AgeResult,
        val nextBirthday: NextBirthdayResult,
        val planetaryAges: List<PlanetaryAge>,
        val isAddingProfile: Boolean = false,
        val editingProfile: UserProfile? = null
    ) : YourTimeUiState
}
