package com.yourtime.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourtime.app.domain.NextBirthdayResult
import com.yourtime.app.ui.theme.CyberCyan
import com.yourtime.app.ui.theme.CyberGradient
import com.yourtime.app.ui.theme.EmeraldGreen
import com.yourtime.app.ui.theme.OledCard
import com.yourtime.app.ui.theme.OledCardBorder
import com.yourtime.app.ui.theme.OledSurface
import com.yourtime.app.ui.theme.TextMuted
import com.yourtime.app.ui.theme.TextWhite
import java.time.format.DateTimeFormatter

@Composable
fun NextBirthdayCard(
    nextBirthday: NextBirthdayResult,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = OledCard),
        border = BorderStroke(1.dp, OledCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (nextBirthday.isToday) Icons.Outlined.Celebration else Icons.Outlined.Event,
                        contentDescription = null,
                        tint = if (nextBirthday.isToday) EmeraldGreen else CyberCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (nextBirthday.isToday) "Happy Birthday!" else "Next Birthday Countdown",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        ),
                        color = TextWhite
                    )
                }

                Text(
                    text = nextBirthday.nextBirthdayDate.format(dateFormatter),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    ),
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (nextBirthday.isToday) {
                // Today is Birthday Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CyberGradient)
                        .padding(vertical = 16.dp, horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎉 Today is the day! Celebrate your journey!",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = TextWhite
                    )
                }
            } else {
                // 4-segment live ticker: Days, Hours, Minutes, Seconds
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CountdownUnit(
                        value = nextBirthday.days,
                        label = "Days",
                        modifier = Modifier.weight(1f)
                    )
                    CountdownUnit(
                        value = nextBirthday.hours,
                        label = "Hours",
                        modifier = Modifier.weight(1f)
                    )
                    CountdownUnit(
                        value = nextBirthday.minutes,
                        label = "Mins",
                        modifier = Modifier.weight(1f)
                    )
                    CountdownUnit(
                        value = nextBirthday.seconds,
                        label = "Secs",
                        isHighlight = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CountdownUnit(
    value: Long,
    label: String,
    isHighlight: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(OledSurface)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = String.format("%02d", value),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = if (isHighlight) EmeraldGreen else CyberCyan
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = TextMuted
            )
        }
    }
}
