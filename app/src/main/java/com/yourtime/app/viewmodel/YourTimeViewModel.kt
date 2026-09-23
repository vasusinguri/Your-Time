package com.yourtime.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yourtime.app.data.PreferencesManager
import com.yourtime.app.domain.AgeCalculator
import com.yourtime.app.domain.NextBirthdayCalculator
import com.yourtime.app.domain.PlanetaryAgeCalculator
import com.yourtime.app.domain.UserProfile
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class YourTimeViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<YourTimeUiState>(YourTimeUiState.Empty())
    val uiState: StateFlow<YourTimeUiState> = _uiState.asStateFlow()

    private var tickerJob: Job? = null
    private var isAppInForeground: Boolean = true

    init {
        observeProfiles()
    }

    private fun observeProfiles() {
        viewModelScope.launch {
            combine(
                preferencesManager.profilesFlow,
                preferencesManager.activeProfileIdFlow
            ) { profiles, activeId ->
                Pair(profiles, activeId)
            }.collect { (profiles, activeId) ->
                if (profiles.isEmpty()) {
                    stopTicker()
                    _uiState.update { current ->
                        if (current is YourTimeUiState.Empty) current else YourTimeUiState.Empty()
                    }
                } else {
                    val active = profiles.firstOrNull { it.id == activeId } ?: profiles.first()
                    val now = LocalDateTime.now()
                    val age = AgeCalculator.calculate(active.birthDateTime, now)
                    val nextBirthday = NextBirthdayCalculator.calculate(active.birthDateTime, now)
                    val planetary = PlanetaryAgeCalculator.calculate(active.birthDateTime, now)

                    _uiState.update { current ->
                        when (current) {
                            is YourTimeUiState.Ready -> current.copy(
                                profiles = profiles,
                                activeProfileId = active.id,
                                activeProfile = active,
                                age = age,
                                nextBirthday = nextBirthday,
                                planetaryAges = planetary
                            )
                            is YourTimeUiState.Empty -> YourTimeUiState.Ready(
                                profiles = profiles,
                                activeProfileId = active.id,
                                activeProfile = active,
                                age = age,
                                nextBirthday = nextBirthday,
                                planetaryAges = planetary
                            )
                        }
                    }
                    startTicker(active.birthDateTime)
                }
            }
        }
    }

    // --- Onboarding / Initial Setup ---

    fun onInitialNameChanged(name: String) {
        _uiState.update { current ->
            if (current is YourTimeUiState.Empty) current.copy(initialName = name) else current
        }
    }

    fun onInitialDateSelected(date: LocalDate) {
        _uiState.update { current ->
            if (current is YourTimeUiState.Empty) current.copy(selectedDate = date, errorMessage = null) else current
        }
    }

    fun onInitialTimeSelected(time: LocalTime) {
        _uiState.update { current ->
            if (current is YourTimeUiState.Empty) current.copy(selectedTime = time, errorMessage = null) else current
        }
    }

    fun createInitialProfile() {
        val current = _uiState.value as? YourTimeUiState.Empty ?: return
        val date = current.selectedDate
        if (date == null) {
            _uiState.update { current.copy(errorMessage = "Please select your date of birth.") }
            return
        }

        val birthDateTime = LocalDateTime.of(date, current.selectedTime)
        val now = LocalDateTime.now()

        if (!AgeCalculator.isValid(birthDateTime, now)) {
            _uiState.update { current.copy(errorMessage = "Birth date & time cannot be in the future.") }
            return
        }

        val newProfile = UserProfile(
            name = current.initialName.trim().ifBlank { "Me" },
            birthDateTime = birthDateTime,
            tag = "Me"
        )

        viewModelScope.launch {
            preferencesManager.addOrUpdateProfile(newProfile)
        }
    }

    // --- Profile Management ---

    fun switchProfile(profileId: String) {
        val current = _uiState.value as? YourTimeUiState.Ready ?: return
        val target = current.profiles.firstOrNull { it.id == profileId } ?: return

        val now = LocalDateTime.now()
        val age = AgeCalculator.calculate(target.birthDateTime, now)
        val nextBirthday = NextBirthdayCalculator.calculate(target.birthDateTime, now)
        val planetary = PlanetaryAgeCalculator.calculate(target.birthDateTime, now)

        _uiState.update {
            current.copy(
                activeProfileId = target.id,
                activeProfile = target,
                age = age,
                nextBirthday = nextBirthday,
                planetaryAges = planetary
            )
        }

        viewModelScope.launch {
            preferencesManager.setActiveProfileId(profileId)
        }

        startTicker(target.birthDateTime)
    }

    fun showAddProfileDialog() {
        _uiState.update { current ->
            if (current is YourTimeUiState.Ready) current.copy(isAddingProfile = true) else current
        }
    }

    fun hideAddProfileDialog() {
        _uiState.update { current ->
            if (current is YourTimeUiState.Ready) current.copy(isAddingProfile = false) else current
        }
    }

    fun addNewProfile(name: String, tag: String, birthDateTime: LocalDateTime) {
        val newProfile = UserProfile(
            name = name.trim().ifBlank { "Profile" },
            birthDateTime = birthDateTime,
            tag = tag.trim().ifBlank { "Family" }
        )
        viewModelScope.launch {
            preferencesManager.addOrUpdateProfile(newProfile)
            hideAddProfileDialog()
        }
    }

    fun startEditingProfile(profile: UserProfile) {
        _uiState.update { current ->
            if (current is YourTimeUiState.Ready) current.copy(editingProfile = profile) else current
        }
    }

    fun cancelEditingProfile() {
        _uiState.update { current ->
            if (current is YourTimeUiState.Ready) current.copy(editingProfile = null) else current
        }
    }

    fun saveEditedProfile(updatedProfile: UserProfile) {
        viewModelScope.launch {
            preferencesManager.addOrUpdateProfile(updatedProfile)
            cancelEditingProfile()
        }
    }

    fun deleteProfile(profileId: String) {
        viewModelScope.launch {
            preferencesManager.deleteProfile(profileId)
        }
    }

    fun resetAll() {
        stopTicker()
        viewModelScope.launch {
            preferencesManager.clearAll()
        }
        _uiState.value = YourTimeUiState.Empty()
    }

    // --- Live Ticker ---

    private fun startTicker(birthDateTime: LocalDateTime) {
        stopTicker()
        if (!isAppInForeground) return

        tickerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                val now = LocalDateTime.now()
                if (AgeCalculator.isValid(birthDateTime, now)) {
                    val age = AgeCalculator.calculate(birthDateTime, now)
                    val nextBirthday = NextBirthdayCalculator.calculate(birthDateTime, now)
                    val planetary = PlanetaryAgeCalculator.calculate(birthDateTime, now)

                    _uiState.update { current ->
                        if (current is YourTimeUiState.Ready && current.activeProfile.birthDateTime == birthDateTime) {
                            current.copy(
                                age = age,
                                nextBirthday = nextBirthday,
                                planetaryAges = planetary
                            )
                        } else {
                            current
                        }
                    }
                }
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
        if (state is YourTimeUiState.Ready) {
            val now = LocalDateTime.now()
            if (AgeCalculator.isValid(state.activeProfile.birthDateTime, now)) {
                val age = AgeCalculator.calculate(state.activeProfile.birthDateTime, now)
                val nextBirthday = NextBirthdayCalculator.calculate(state.activeProfile.birthDateTime, now)
                val planetary = PlanetaryAgeCalculator.calculate(state.activeProfile.birthDateTime, now)
                _uiState.update {
                    state.copy(
                        age = age,
                        nextBirthday = nextBirthday,
                        planetaryAges = planetary
                    )
                }
            }
            startTicker(state.activeProfile.birthDateTime)
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
