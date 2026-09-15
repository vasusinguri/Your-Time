package com.yourtime.app.viewmodel

import com.yourtime.app.domain.AgeResult
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * UI State representing the screens and states of Your Time.
 */
sealed interface YourTimeUiState {

    /**
     * Initial state: Prompting user to enter birth date and time.
     */
    data class Initial(
        val selectedDate: LocalDate? = null,
        val selectedTime: LocalTime = LocalTime.MIDNIGHT,
        val errorMessage: String? = null
    ) : YourTimeUiState

    /**
     * Calculated state: Real-time live age ticker active.
     */
    data class Calculated(
        val birthDateTime: LocalDateTime,
        val age: AgeResult,
        val isEditing: Boolean = false,
        val editDate: LocalDate? = null,
        val editTime: LocalTime? = null,
        val editErrorMessage: String? = null
    ) : YourTimeUiState
}
