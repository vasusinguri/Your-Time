package com.yourtime.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yourtime.app.data.PreferencesManager
import com.yourtime.app.domain.AgeCalculator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class YourTimeViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<YourTimeUiState>(YourTimeUiState.Initial())
    val uiState: StateFlow<YourTimeUiState> = _uiState.asStateFlow()

    private var tickerJob: Job? = null
    private var isAppInForeground: Boolean = true

    init {
        loadSavedBirthDateTime()
    }

    private fun loadSavedBirthDateTime() {
        viewModelScope.launch {
            val saved = preferencesManager.birthDateTimeFlow.firstOrNull()
            if (saved != null) {
                if (AgeCalculator.isValid(saved)) {
                    val age = AgeCalculator.calculate(saved)
                    _uiState.value = YourTimeUiState.Calculated(
                        birthDateTime = saved,
                        age = age
                    )
                    startTicker(saved)
                } else {
                    _uiState.value = YourTimeUiState.Initial(
                        errorMessage = "Saved birth date was in the future. Please re-enter."
                    )
                }
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { current ->
            when (current) {
                is YourTimeUiState.Initial -> current.copy(selectedDate = date, errorMessage = null)
                is YourTimeUiState.Calculated -> current.copy(editDate = date, editErrorMessage = null)
            }
        }
    }

    fun onTimeSelected(time: LocalTime) {
        _uiState.update { current ->
            when (current) {
                is YourTimeUiState.Initial -> current.copy(selectedTime = time, errorMessage = null)
                is YourTimeUiState.Calculated -> current.copy(editTime = time, editErrorMessage = null)
            }
        }
    }

    fun calculate() {
        val currentState = _uiState.value as? YourTimeUiState.Initial ?: return
        val date = currentState.selectedDate
        if (date == null) {
            _uiState.update { currentState.copy(errorMessage = "Please select your date of birth.") }
            return
        }

        val time = currentState.selectedTime
        val birthDateTime = LocalDateTime.of(date, time)
        val now = LocalDateTime.now()

        if (!AgeCalculator.isValid(birthDateTime, now)) {
            _uiState.update {
                currentState.copy(
                    errorMessage = "Birth date & time cannot be in the future."
                )
            }
            return
        }

        val initialAge = AgeCalculator.calculate(birthDateTime, now)
        _uiState.value = YourTimeUiState.Calculated(
            birthDateTime = birthDateTime,
            age = initialAge
        )

        viewModelScope.launch {
            preferencesManager.saveBirthDateTime(birthDateTime)
        }

        startTicker(birthDateTime)
    }

    fun startEditing() {
        val current = _uiState.value as? YourTimeUiState.Calculated ?: return
        _uiState.update {
            current.copy(
                isEditing = true,
                editDate = current.birthDateTime.toLocalDate(),
                editTime = current.birthDateTime.toLocalTime(),
                editErrorMessage = null
            )
        }
    }

    fun cancelEditing() {
        val current = _uiState.value as? YourTimeUiState.Calculated ?: return
        _uiState.update {
            current.copy(
                isEditing = false,
                editDate = null,
                editTime = null,
                editErrorMessage = null
            )
        }
    }

    fun saveEditing() {
        val current = _uiState.value as? YourTimeUiState.Calculated ?: return
        val newDate = current.editDate ?: current.birthDateTime.toLocalDate()
        val newTime = current.editTime ?: current.birthDateTime.toLocalTime()
        val newBirthDateTime = LocalDateTime.of(newDate, newTime)
        val now = LocalDateTime.now()

        if (!AgeCalculator.isValid(newBirthDateTime, now)) {
            _uiState.update {
                current.copy(editErrorMessage = "Birth date & time cannot be in the future.")
            }
            return
        }

        val updatedAge = AgeCalculator.calculate(newBirthDateTime, now)
        _uiState.value = YourTimeUiState.Calculated(
            birthDateTime = newBirthDateTime,
            age = updatedAge,
            isEditing = false
        )

        viewModelScope.launch {
            preferencesManager.saveBirthDateTime(newBirthDateTime)
        }

        startTicker(newBirthDateTime)
    }

    fun reset() {
        stopTicker()
        viewModelScope.launch {
            preferencesManager.clearBirthDateTime()
        }
        _uiState.value = YourTimeUiState.Initial()
    }

    private fun startTicker(birthDateTime: LocalDateTime) {
        stopTicker()
        if (!isAppInForeground) return

        tickerJob = viewModelScope.launch {
            while (isActive) {
                val now = LocalDateTime.now()
                if (AgeCalculator.isValid(birthDateTime, now)) {
                    val currentAge = AgeCalculator.calculate(birthDateTime, now)
                    _uiState.update { state ->
                        if (state is YourTimeUiState.Calculated) {
                            state.copy(age = currentAge)
                        } else {
                            state
                        }
                    }
                }
                delay(1000L)
            }
        }
    }

    private fun stopTicker() {
        tickerJob?.cancel()
        tickerJob = null
    }

    fun onForegroundResume() {
        isAppInForeground = true
        val state = _uiState.value
        if (state is YourTimeUiState.Calculated) {
            // Immediate recalculation upon returning to foreground to prevent any drift
            val now = LocalDateTime.now()
            if (AgeCalculator.isValid(state.birthDateTime, now)) {
                val updatedAge = AgeCalculator.calculate(state.birthDateTime, now)
                _uiState.update { state.copy(age = updatedAge) }
            }
            startTicker(state.birthDateTime)
        }
    }

    fun onBackgroundPause() {
        isAppInForeground = false
        stopTicker()
    }

    override fun onCleared() {
        super.onCleared()
        stopTicker()
    }

    class Factory(private val preferencesManager: PreferencesManager) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(YourTimeViewModel::class.java)) {
                return YourTimeViewModel(preferencesManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
