package com.yourtime.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourtime.app.domain.AgeResult
import com.yourtime.app.ui.theme.CyberCyan
import com.yourtime.app.ui.theme.EmeraldGreen
import com.yourtime.app.ui.theme.OledCard
import com.yourtime.app.ui.theme.OledCardBorder
import com.yourtime.app.ui.theme.TextMuted

@Composable
fun AgeDisplayCards(
    age: AgeResult,
    modifier: Modifier = Modifier
) {
    val timeUnits = listOf(
        TimeUnitItem(value = age.years, label = "Years", icon = Icons.Outlined.CalendarToday),
        TimeUnitItem(value = age.months, label = "Months", icon = Icons.Outlined.DateRange),
        TimeUnitItem(value = age.days, label = "Days", icon = Icons.Outlined.CalendarToday),
        TimeUnitItem(value = age.hours, label = "Hours", icon = Icons.Outlined.Schedule),
        TimeUnitItem(value = age.minutes, label = "Minutes", icon = Icons.Outlined.WatchLater),
        TimeUnitItem(value = age.seconds, label = "Seconds", icon = Icons.Outlined.Timer, isSeconds = true)
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TimeUnitCard(data = timeUnits[0], modifier = Modifier.weight(1f))
            TimeUnitCard(data = timeUnits[1], modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TimeUnitCard(data = timeUnits[2], modifier = Modifier.weight(1f))
            TimeUnitCard(data = timeUnits[3], modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TimeUnitCard(data = timeUnits[4], modifier = Modifier.weight(1f))
            TimeUnitCard(data = timeUnits[5], modifier = Modifier.weight(1f))
        }
    }
}

private data class TimeUnitItem(
    val value: Long,
    val label: String,
    val icon: ImageVector,
    val isSeconds: Boolean = false
)

@Composable
private fun TimeUnitCard(
    data: TimeUnitItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = OledCard
        ),
        border = BorderStroke(1.dp, OledCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 22.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Neon Digits
            Text(
                text = "${data.value}",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                ),
                color = if (data.isSeconds) EmeraldGreen else CyberCyan
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Icon + Label
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = data.icon,
                    contentDescription = null,
                    tint = if (data.isSeconds) EmeraldGreen.copy(alpha = 0.7f) else CyberCyan.copy(alpha = 0.7f),
                    modifier = Modifier.size(15.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = data.label,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = TextMuted
                )
            }
        }
    }
}
