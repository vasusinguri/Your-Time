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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.Insights
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yourtime.app.ui.components.AgeDisplayCards
import com.yourtime.app.ui.components.DatePickerModal
import com.yourtime.app.ui.components.TimePickerModal
import com.yourtime.app.ui.theme.BackgroundDark
import com.yourtime.app.ui.theme.CardBackground
import com.yourtime.app.ui.theme.CardBorder
import com.yourtime.app.ui.theme.CoralPrimary
import com.yourtime.app.ui.theme.LiveGreen
import com.yourtime.app.ui.theme.SunsetGradient
import com.yourtime.app.ui.theme.SurfaceDark
import com.yourtime.app.ui.theme.TextMuted
import com.yourtime.app.ui.theme.TextSubtitle
import com.yourtime.app.ui.theme.TextWhite
import com.yourtime.app.viewmodel.YourTimeUiState
import com.yourtime.app.viewmodel.YourTimeViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

enum class AppTab {
    Home,
    Insights
}

@Composable
fun YourTimeScreen(
    viewModel: YourTimeViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf(AppTab.Home) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    val calculatedState = uiState as? YourTimeUiState.Calculated

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        topBar = {
            YourTimeTopBar(
                onShare = {
                    calculatedState?.let { calc ->
                        val age = calc.age
                        val shareText = "I have been alive for ${age.years} years, ${age.months} months, " +
                                "${age.days} days, ${age.hours} hours, ${age.minutes} minutes and ${age.seconds} seconds!\n" +
                                "Track your journey with Your Time."
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share Your Time"))
                    }
                },
                onOpenMenu = { showMenu = true },
                isCalculated = calculatedState != null,
                showMenu = showMenu,
                onDismissMenu = { showMenu = false },
                onEdit = {
                    showMenu = false
                    viewModel.startEditing()
                },
                onReset = {
                    showMenu = false
                    showResetDialog = true
                }
            )
        },
        bottomBar = {
            YourTimeBottomNav(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AppTab.Home -> {
                    HomeTabContent(
                        uiState = uiState,
                        onOpenDatePicker = { showDatePicker = true },
                        onOpenTimePicker = { showTimePicker = true },
                        onCalculate = { viewModel.calculate() }
                    )
                }

                AppTab.Insights -> {
                    InsightsScreen(age = calculatedState?.age)
                }
            }
        }
    }

    // Date Picker Modal
    if (showDatePicker) {
        val initialDate = when (val state = uiState) {
            is YourTimeUiState.Initial -> state.selectedDate
            is YourTimeUiState.Calculated -> state.editDate ?: state.birthDateTime.toLocalDate()
        }
        DatePickerModal(
            onDateSelected = { date ->
                viewModel.onDateSelected(date)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
            initialDate = initialDate
        )
    }

    // Time Picker Modal
    if (showTimePicker) {
        val initialTime = when (val state = uiState) {
            is YourTimeUiState.Initial -> state.selectedTime
            is YourTimeUiState.Calculated -> state.editTime ?: state.birthDateTime.toLocalTime()
        }
        TimePickerModal(
            onTimeSelected = { time ->
                viewModel.onTimeSelected(time)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false },
            initialTime = initialTime
        )
    }

    // Reset Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Birth Time?", color = TextWhite) },
            text = { Text("This will clear your saved birth date and time from this device.", color = TextMuted) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.reset()
                        showResetDialog = false
                    }
                ) {
                    Text("Reset", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel", color = TextWhite)
                }
            },
            containerColor = CardBackground
        )
    }

    // Edit Birth Date / Time Dialog (when user taps Edit in calculated state)
    if (calculatedState?.isEditing == true) {
        EditDetailsDialog(
            state = calculatedState,
            onOpenDatePicker = { showDatePicker = true },
            onOpenTimePicker = { showTimePicker = true },
            onSave = { viewModel.saveEditing() },
            onReset = {
                viewModel.cancelEditing()
                showResetDialog = true
            },
            onCancel = { viewModel.cancelEditing() }
        )
    }
}

@Composable
private fun YourTimeTopBar(
    onShare: () -> Unit,
    onOpenMenu: () -> Unit,
    isCalculated: Boolean,
    showMenu: Boolean,
    onDismissMenu: () -> Unit,
    onEdit: () -> Unit,
    onReset: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Title Logo: "Your Time"
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Your ",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = TextWhite
            )
            Text(
                text = "Time",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = CoralPrimary
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isCalculated) {
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
                    modifier = Modifier.background(CardBackground)
                ) {
                    if (isCalculated) {
                        DropdownMenuItem(
                            text = { Text("Edit Birth Date/Time", color = TextWhite) },
                            onClick = onEdit
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("Reset", color = MaterialTheme.colorScheme.error) },
                        onClick = onReset
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeTabContent(
    uiState: YourTimeUiState,
    onOpenDatePicker: () -> Unit,
    onOpenTimePicker: () -> Unit,
    onCalculate: () -> Unit
) {
    when (val state = uiState) {
        is YourTimeUiState.Initial -> {
            InputScreenContent(
                state = state,
                onOpenDatePicker = onOpenDatePicker,
                onOpenTimePicker = onOpenTimePicker,
                onCalculate = onCalculate
            )
        }

        is YourTimeUiState.Calculated -> {
            CalculatedScreenContent(state = state)
        }
    }
}

@Composable
private fun InputScreenContent(
    state: YourTimeUiState.Initial,
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
            text = "Let's calculate your journey.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Date of Birth Card
        InputCard(
            label = "Date of Birth",
            valueText = state.selectedDate?.format(dateFormatter) ?: "Select Date",
            isPlaceholder = state.selectedDate == null,
            icon = Icons.Outlined.CalendarToday,
            onClick = onOpenDatePicker
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Time of Birth Card
        InputCard(
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

        Spacer(modifier = Modifier.height(32.dp))

        // Sunset Gradient Button: "Calculate My Time ->"
        Button(
            onClick = onCalculate,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(SunsetGradient, shape = RoundedCornerShape(16.dp)),
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

        Spacer(modifier = Modifier.height(36.dp))

        // Philosophical quote
        Text(
            text = "“Time reveals everything.”",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontStyle = FontStyle.Italic
            ),
            color = TextMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun CalculatedScreenContent(
    state: YourTimeUiState.Calculated
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // "● Live" Badge
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = CardBackground,
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(LiveGreen, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Live",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    ),
                    color = TextWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You are",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            ),
            color = TextSubtitle
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 6 Age Display Cards (Years, Months, Days, Hours, Minutes, Seconds)
        AgeDisplayCards(age = state.age)

        Spacer(modifier = Modifier.height(36.dp))

        // Quote at bottom: "Every second counts."
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
private fun InputCard(
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
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, CardBorder)
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
                    tint = TextMuted.copy(alpha = 0.6f),
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

@Composable
private fun EditDetailsDialog(
    state: YourTimeUiState.Calculated,
    onOpenDatePicker: () -> Unit,
    onOpenTimePicker: () -> Unit,
    onSave: () -> Unit,
    onReset: () -> Unit,
    onCancel: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    val currentDate = state.editDate ?: state.birthDateTime.toLocalDate()
    val currentTime = state.editTime ?: state.birthDateTime.toLocalTime()

    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Column {
                Text(
                    text = "Edit Your Details",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "You can update your birth date and time anytime.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                InputCard(
                    label = "Date of Birth",
                    valueText = currentDate.format(dateFormatter),
                    isPlaceholder = false,
                    icon = Icons.Outlined.CalendarToday,
                    onClick = onOpenDatePicker
                )

                Spacer(modifier = Modifier.height(12.dp))

                InputCard(
                    label = "Time of Birth",
                    valueText = currentTime.format(timeFormatter),
                    isPlaceholder = false,
                    icon = Icons.Outlined.Schedule,
                    onClick = onOpenTimePicker
                )

                state.editErrorMessage?.let { err ->
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = err,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(SunsetGradient, shape = RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Changes", color = TextWhite, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onReset,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CardBorder)
                ) {
                    Text("Reset", color = TextWhite)
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel", color = TextMuted)
            }
        },
        containerColor = SurfaceDark
    )
}

@Composable
private fun YourTimeBottomNav(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    NavigationBar(
        containerColor = BackgroundDark,
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        NavigationBarItem(
            selected = selectedTab == AppTab.Home,
            onClick = { onTabSelected(AppTab.Home) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CoralPrimary,
                selectedTextColor = CoralPrimary,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedTab == AppTab.Insights,
            onClick = { onTabSelected(AppTab.Insights) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Insights,
                    contentDescription = "Insights"
                )
            },
            label = { Text("Insights") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CoralPrimary,
                selectedTextColor = CoralPrimary,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = Color.Transparent
            )
        )
    }
}
