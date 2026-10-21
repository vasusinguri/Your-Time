package com.yourtime.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.HourglassBottom
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourtime.app.domain.AgeResult
import com.yourtime.app.ui.theme.CyberCyan
import com.yourtime.app.ui.theme.EmeraldGreen
import com.yourtime.app.ui.theme.OledCard
import com.yourtime.app.ui.theme.OledCardBorder
import com.yourtime.app.ui.theme.OledSurface
import com.yourtime.app.ui.theme.TextMuted
import com.yourtime.app.ui.theme.TextWhite

@Composable
fun AgeDisplayCards(
    age: AgeResult,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Liquid Time Stream (Fluid Flow Progress Capsule)
        LiquidTimeStreamBar(seconds = age.seconds)

        // Precision Chronometer Cards (3 rows of 2 units)
        // Direct parameter passing ensures Compose skips unchanged cards on each second tick!
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TimeUnitCard(
                value = age.years,
                label = "YEARS",
                icon = Icons.Outlined.CalendarToday,
                isSeconds = false,
                modifier = Modifier.weight(1f)
            )
            TimeUnitCard(
                value = age.months,
                label = "MONTHS",
                icon = Icons.Outlined.DateRange,
                isSeconds = false,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TimeUnitCard(
                value = age.days,
                label = "DAYS",
                icon = Icons.Outlined.CalendarToday,
                isSeconds = false,
                modifier = Modifier.weight(1f)
            )
            TimeUnitCard(
                value = age.hours,
                label = "HOURS",
                icon = Icons.Outlined.Schedule,
                isSeconds = false,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TimeUnitCard(
                value = age.minutes,
                label = "MINUTES",
                icon = Icons.Outlined.HourglassBottom,
                isSeconds = false,
                modifier = Modifier.weight(1f)
            )
            TimeUnitCard(
                value = age.seconds,
                label = "SECONDS",
                icon = Icons.Outlined.Timer,
                isSeconds = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Highly optimized, zero-allocation time unit card.
 * Receives primitive [value] so Compose skips recomposing years/months/days/hours/minutes when unchanged.
 */
@Composable
private fun TimeUnitCard(
    value: Long,
    label: String,
    icon: ImageVector,
    isSeconds: Boolean,
    modifier: Modifier = Modifier
) {
    val cardBorder = if (isSeconds) {
        BorderStroke(
            1.2.dp,
            Brush.horizontalGradient(
                listOf(
                    EmeraldGreen.copy(alpha = 0.65f),
                    CyberCyan.copy(alpha = 0.35f)
                )
            )
        )
    } else {
        BorderStroke(1.dp, OledCardBorder)
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = OledCard),
        border = cardBorder
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Neon Digits with tabular font feature to eliminate horizontal jitter
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    fontFeatureSettings = "tnum",
                    letterSpacing = (-0.5).sp
                ),
                color = if (isSeconds) EmeraldGreen else CyberCyan
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Sub-label with uppercase tracking & icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSeconds) EmeraldGreen.copy(alpha = 0.8f) else CyberCyan.copy(alpha = 0.8f),
                    modifier = Modifier.size(13.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.2.sp
                    ),
                    color = TextMuted
                )
            }
        }
    }
}

/**
 * Liquid Time Stream: A fluid, flowing dynamic meter that visually flows with the seconds.
 * Features an infinite shimmering gradient and animated fill ratio.
 */
@Composable
private fun LiquidTimeStreamBar(
    seconds: Long,
    modifier: Modifier = Modifier
) {
    val currentSecondOfMinute = (seconds % 60L).toFloat()
    val targetRatio = (currentSecondOfMinute / 60f).coerceIn(0.02f, 1f)

    val animatedRatio by animateFloatAsState(
        targetValue = targetRatio,
        animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing),
        label = "LiquidRatio"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "LiquidShimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerOffset"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = OledCard),
        border = BorderStroke(1.dp, OledCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // Header: Status & Live seconds count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(EmeraldGreen, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = "LIQUID TIME STREAM",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.2.sp
                        ),
                        color = TextWhite
                    )
                }

                Text(
                    text = "${seconds % 60}s / 60s",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        fontFeatureSettings = "tnum"
                    ),
                    color = EmeraldGreen
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Fluid progress capsule
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(OledSurface)
            ) {
                val fluidBrush = Brush.linearGradient(
                    colors = listOf(
                        CyberCyan,
                        Color(0xFF06B6D4),
                        EmeraldGreen,
                        CyberCyan
                    ),
                    start = Offset(shimmerOffset, 0f),
                    end = Offset(shimmerOffset + 400f, 0f)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = animatedRatio)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(fluidBrush)
                )
            }
        }
    }
}
