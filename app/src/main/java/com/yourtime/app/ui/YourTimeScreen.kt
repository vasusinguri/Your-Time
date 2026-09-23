package com.yourtime.app.ui

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yourtime.app.ui.components.AddEditProfileDialog
import com.yourtime.app.ui.components.AgeDisplayCards
import com.yourtime.app.ui.components.DatePickerModal
import com.yourtime.app.ui.components.NextBirthdayCard
import com.yourtime.app.ui.components.PlanetaryAgeSection
import com.yourtime.app.ui.components.ProfileBar
import com.yourtime.app.ui.components.TimePickerModal
import com.yourtime.app.ui.theme.CyberCyan
import com.yourtime.app.ui.theme.CyberGradient
import com.yourtime.app.ui.theme.EmeraldGreen
import com.yourtime.app.ui.theme.OledBlack
import com.yourtime.app.ui.theme.OledCard
import com.yourtime.app.ui.theme.OledCardBorder
import com.yourtime.app.ui.theme.OledSurface
import com.yourtime.app.ui.theme.TextMuted
import com.yourtime.app.ui.theme.TextSubtitle
import com.yourtime.app.ui.theme.TextWhite
import com.yourtime.app.viewmodel.YourTimeUiState
import com.yourtime.app.viewmodel.YourTimeViewModel
import java.time.format.DateTimeFormatter

@Composable
fun YourTimeScreen(
    viewModel: YourTimeViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    val readyState = uiState as? YourTimeUiState.Ready

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        containerColor = OledBlack,
        topBar = {
            YourTimeTopBar(
                onShare = {
                    readyState?.let { state ->
                        val age = state.age
                        val profileName = state.activeProfile.name
                        val shareText = "According to Your Time, $profileName has been alive for " +
                                "${age.years} years, ${age.months} months, ${age.days} days, " +
                                "${age.hours} hours, ${age.minutes} minutes and ${age.seconds} seconds!\n" +
                                "Next birthday in ${state.nextBirthday.days} days!"
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share Your Time"))
                    }
                },
                onOpenMenu = { showMenu = true },
                isReady = readyState != null,
                showMenu = showMenu,
                onDismissMenu = { showMenu = false },
                onEditProfile = {
                    showMenu = false
                    readyState?.let { viewModel.startEditingProfile(it.activeProfile) }
                },
                onResetAll = {
                    showMenu = false
                    showResetDialog = true
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is YourTimeUiState.Empty -> {
                    EmptyScreenContent(
                        state = state,
                        onNameChanged = { viewModel.onInitialNameChanged(it) },
                        onOpenDatePicker = { showDatePicker = true },
                        onOpenTimePicker = { showTimePicker = true },
                        onCalculate = { viewModel.createInitialProfile() }
                    )
                }

                is YourTimeUiState.Ready -> {
                    ReadyScreenContent(
                        state = state,
                        onSelectProfile = { viewModel.switchProfile(it) },
                        onAddProfile = { viewModel.showAddProfileDialog() },
                        onEditActiveProfile = { viewModel.startEditingProfile(state.activeProfile) }
                    )
                }
            }
        }
    }

    // Initial Date Picker Modal
    if (showDatePicker) {
        val emptyState = uiState as? YourTimeUiState.Empty
        DatePickerModal(
            onDateSelected = { date ->
                viewModel.onInitialDateSelected(date)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
            initialDate = emptyState?.selectedDate
        )
    }

    // Initial Time Picker Modal
    if (showTimePicker) {
        val emptyState = uiState as? YourTimeUiState.Empty
        TimePickerModal(
            onTimeSelected = { time ->
                viewModel.onInitialTimeSelected(time)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false },
            initialTime = emptyState?.selectedTime ?: java.time.LocalTime.MIDNIGHT
        )
    }

    // Add Profile Dialog
    if (readyState?.isAddingProfile == true) {
        AddEditProfileDialog(
            profileToEdit = null,
            canDelete = false,
            onSave = { name, tag, birthDateTime ->
                viewModel.addNewProfile(name, tag, birthDateTime)
            },
            onDismiss = { viewModel.hideAddProfileDialog() }
        )
    }

    // Edit Profile Dialog
    readyState?.editingProfile?.let { profileToEdit ->
        AddEditProfileDialog(
            profileToEdit = profileToEdit,
            canDelete = readyState.profiles.size > 1,
            onSave = { name, tag, birthDateTime ->
                viewModel.saveEditedProfile(
                    profileToEdit.copy(
                        name = name,
                        tag = tag,
                        birthDateTime = birthDateTime
                    )
                )
            },
            onDelete = {
                viewModel.deleteProfile(profileToEdit.id)
                viewModel.cancelEditingProfile()
            },
            onDismiss = { viewModel.cancelEditingProfile() }
        )
    }

    // Reset All Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset All Data?", color = TextWhite) },
            text = {
                Text(
                    "This will clear all saved profiles and time history from this device.",
                    color = TextMuted
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetAll()
                        showResetDialog = false
                    }
                ) {
                    Text("Reset All", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel", color = TextWhite)
                }
            },
            containerColor = OledCard
        )
    }
}

@Composable
private fun YourTimeTopBar(
    onShare: () -> Unit,
    onOpenMenu: () -> Unit,
    isReady: Boolean,
    showMenu: Boolean,
    onDismissMenu: () -> Unit,
    onEditProfile: () -> Unit,
    onResetAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Title Logo: "Your Time"
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Your ",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                ),
                color = TextWhite
            )
            Text(
                text = "Time",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                ),
                color = CyberCyan
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isReady) {
                IconButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Box {
                IconButton(onClick = onOpenMenu) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = onDismissMenu,
                    modifier = Modifier.background(OledCard)
                ) {
                    if (isReady) {
                        DropdownMenuItem(
                            text = { Text("Edit Active Profile", color = TextWhite) },
                            onClick = onEditProfile
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("Reset All", color = MaterialTheme.colorScheme.error) },
                        onClick = onResetAll
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyScreenContent(
    state: YourTimeUiState.Empty,
    onNameChanged: (String) -> Unit,
    onOpenDatePicker: () -> Unit,
    onOpenTimePicker: () -> Unit,
    onCalculate: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Track your existence across time and space.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(26.dp))

        // Profile Name Input
        OutlinedTextField(
            value = state.initialName,
            onValueChange = onNameChanged,
            label = { Text("Your Name", color = TextMuted) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyberCyan,
                unfocusedBorderColor = OledCardBorder,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                cursorColor = CyberCyan,
                focusedContainerColor = OledSurface,
                unfocusedContainerColor = OledSurface
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Date of Birth Card
        InputPickerCard(
            label = "Date of Birth",
            valueText = state.selectedDate?.format(dateFormatter) ?: "Select Date",
            isPlaceholder = state.selectedDate == null,
            icon = Icons.Outlined.CalendarToday,
            onClick = onOpenDatePicker
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Time of Birth Card
        InputPickerCard(
            label = "Time of Birth",
            valueText = state.selectedTime.format(timeFormatter),
            isPlaceholder = false,
            icon = Icons.Outlined.Schedule,
            onClick = onOpenTimePicker
        )

        AnimatedVisibility(
            visible = state.errorMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Cyber Gradient Button: "Calculate My Time ->"
        Button(
            onClick = onCalculate,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(CyberGradient, shape = RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Calculate My Time",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = TextWhite
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "“Time reveals everything.”",
            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
            color = TextMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun ReadyScreenContent(
    state: YourTimeUiState.Ready,
    onSelectProfile: (String) -> Unit,
    onAddProfile: () -> Unit,
    onEditActiveProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Multi-Profile Bar
        ProfileBar(
            profiles = state.profiles,
            activeProfileId = state.activeProfileId,
            onSelectProfile = onSelectProfile,
            onAddProfile = onAddProfile
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Active Profile Header & Live Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = state.activeProfile.name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = TextWhite
                )
                Text(
                    text = "Profile • ${state.activeProfile.tag}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = TextMuted
                )
            }

            // Live Pill Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = OledCard,
                border = BorderStroke(1.dp, OledCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(EmeraldGreen, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = TextWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 6 Age Display Cards (Years, Months, Days, Hours, Minutes, Seconds)
        AgeDisplayCards(age = state.age)

        Spacer(modifier = Modifier.height(24.dp))

        // Next Birthday Countdown Card
        NextBirthdayCard(nextBirthday = state.nextBirthday)

        Spacer(modifier = Modifier.height(28.dp))

        // Planetary Age Section (Mercury, Venus, Earth, Mars, Jupiter, Saturn)
        PlanetaryAgeSection(planetaryAges = state.planetaryAges)

        Spacer(modifier = Modifier.height(36.dp))

        // Quote
        Text(
            text = "“Every second counts.”",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium
            ),
            color = TextSubtitle,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun InputPickerCard(
    label: String,
    valueText: String,
    isPlaceholder: Boolean,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = OledCard),
        border = BorderStroke(1.dp, OledCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = TextMuted
                    )
                }

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CyberCyan.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = valueText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp
                ),
                color = if (isPlaceholder) TextMuted else TextWhite
            )
        }
    }
}
