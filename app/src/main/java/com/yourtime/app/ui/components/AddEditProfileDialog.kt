package com.yourtime.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourtime.app.domain.AgeCalculator
import com.yourtime.app.domain.UserProfile
import com.yourtime.app.ui.theme.CyberCyan
import com.yourtime.app.ui.theme.CyberGradient
import com.yourtime.app.ui.theme.EmeraldGreen
import com.yourtime.app.ui.theme.ErrorRed
import com.yourtime.app.ui.theme.OledCard
import com.yourtime.app.ui.theme.OledCardBorder
import com.yourtime.app.ui.theme.OledSurface
import com.yourtime.app.ui.theme.TextMuted
import com.yourtime.app.ui.theme.TextWhite
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun AddEditProfileDialog(
    profileToEdit: UserProfile? = null,
    canDelete: Boolean = false,
    onSave: (name: String, tag: String, birthDateTime: LocalDateTime) -> Unit,
    onDelete: () -> Unit = {},
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(profileToEdit?.name ?: "") }
    var selectedTag by remember { mutableStateOf(profileToEdit?.tag ?: if (profileToEdit == null) "Family" else "Me") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(profileToEdit?.birthDateTime?.toLocalDate()) }
    var selectedTime by remember { mutableStateOf(profileToEdit?.birthDateTime?.toLocalTime() ?: LocalTime.MIDNIGHT) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    val tags = listOf("Me", "Partner", "Family", "Friend")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (profileToEdit == null) "Add New Profile" else "Edit Profile",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextWhite
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Name Input
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = null
                    },
                    label = { Text("Name", color = TextMuted) },
                    placeholder = { Text("e.g. Sarah", color = TextMuted) },
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
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Tag Chips
                Column {
                    Text(
                        text = "Relationship Tag",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tags.forEach { tag ->
                            val isSelected = selectedTag.equals(tag, ignoreCase = true)
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) CyberCyan.copy(alpha = 0.2f) else OledSurface,
                                border = BorderStroke(1.dp, if (isSelected) CyberCyan else OledCardBorder),
                                modifier = Modifier.clickable { selectedTag = tag }
                            ) {
                                Text(
                                    text = tag,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                    color = if (isSelected) CyberCyan else TextMuted
                                )
                            }
                        }
                    }
                }

                // Date Picker trigger
                DialogPickerCard(
                    label = "Date of Birth",
                    valueText = selectedDate?.format(dateFormatter) ?: "Select Date",
                    isPlaceholder = selectedDate == null,
                    icon = Icons.Outlined.CalendarToday,
                    onClick = { showDatePicker = true }
                )

                // Time Picker trigger
                DialogPickerCard(
                    label = "Time of Birth",
                    valueText = selectedTime.format(timeFormatter),
                    isPlaceholder = false,
                    icon = Icons.Outlined.Schedule,
                    onClick = { showTimePicker = true }
                )

                AnimatedVisibility(visible = errorMessage != null) {
                    errorMessage?.let { err ->
                        Text(
                            text = err,
                            color = ErrorRed,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Save Button
                Button(
                    onClick = {
                        val trimmedName = name.trim()
                        if (trimmedName.isEmpty()) {
                            errorMessage = "Please enter a profile name."
                            return@Button
                        }
                        val date = selectedDate
                        if (date == null) {
                            errorMessage = "Please select date of birth."
                            return@Button
                        }
                        val birthDateTime = LocalDateTime.of(date, selectedTime)
                        if (!AgeCalculator.isValid(birthDateTime)) {
                            errorMessage = "Birth date cannot be in the future."
                            return@Button
                        }
                        onSave(trimmedName, selectedTag, birthDateTime)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(CyberGradient, shape = RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (profileToEdit == null) "Save Profile" else "Save Changes",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (profileToEdit != null && canDelete) {
                    OutlinedButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.6f))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null,
                                tint = ErrorRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Delete Profile", color = ErrorRed, fontSize = 13.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        },
        containerColor = OledCard
    )

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { date ->
                selectedDate = date
                errorMessage = null
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
            initialDate = selectedDate
        )
    }

    if (showTimePicker) {
        TimePickerModal(
            onTimeSelected = { time ->
                selectedTime = time
                errorMessage = null
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false },
            initialTime = selectedTime
        )
    }
}

@Composable
private fun DialogPickerCard(
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = OledSurface),
        border = BorderStroke(1.dp, OledCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = valueText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = if (isPlaceholder) TextMuted else TextWhite
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CyberCyan,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
