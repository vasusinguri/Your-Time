package com.yourtime.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourtime.app.ui.theme.AmberSecondary
import com.yourtime.app.ui.theme.CoralPrimary

@Composable
fun GlowingHourglassCard(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hourglass_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Callouts
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(70.dp)
            ) {
                CalloutText(text = "More\nMoments")
                CalloutText(text = "Deeper\nExperiences")
            }

            // Center Animated Glowing Hourglass
            Box(
                modifier = Modifier
                    .size(160.dp, 210.dp)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(140.dp, 190.dp)) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2f
                    val cy = h / 2f

                    // Subtle ambient glow behind center neck
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                CoralPrimary.copy(alpha = 0.35f * glowAlpha),
                                Color.Transparent
                            ),
                            center = Offset(cx, cy),
                            radius = w * 0.75f
                        ),
                        radius = w * 0.75f
                    )

                    // Top & bottom caps
                    val capWidth = w * 0.85f
                    val capHeight = 10f
                    drawRoundRect(
                        color = Color(0xFF263246),
                        topLeft = Offset(cx - capWidth / 2f, 0f),
                        size = Size(capWidth, capHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(5f, 5f)
                    )
                    drawRoundRect(
                        color = Color(0xFF263246),
                        topLeft = Offset(cx - capWidth / 2f, h - capHeight),
                        size = Size(capWidth, capHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(5f, 5f)
                    )

                    // Glass Body Outline
                    val glassPath = Path().apply {
                        moveTo(cx - capWidth * 0.42f, capHeight)
                        cubicTo(
                            cx - capWidth * 0.42f, cy * 0.6f,
                            cx - 16f, cy * 0.9f,
                            cx - 10f, cy
                        )
                        cubicTo(
                            cx - 16f, cy * 1.1f,
                            cx - capWidth * 0.42f, cy * 1.4f,
                            cx - capWidth * 0.42f, h - capHeight
                        )
                        lineTo(cx + capWidth * 0.42f, h - capHeight)
                        cubicTo(
                            cx + capWidth * 0.42f, cy * 1.4f,
                            cx + 16f, cy * 1.1f,
                            cx + 10f, cy
                        )
                        cubicTo(
                            cx + 16f, cy * 0.9f,
                            cx + capWidth * 0.42f, cy * 0.6f,
                            cx + capWidth * 0.42f, capHeight
                        )
                        close()
                    }

                    drawPath(
                        path = glassPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                CoralPrimary.copy(alpha = 0.7f),
                                AmberSecondary.copy(alpha = 0.9f * glowAlpha),
                                CoralPrimary.copy(alpha = 0.7f)
                            )
                        ),
                        style = Stroke(width = 3.5f, cap = StrokeCap.Round)
                    )

                    // Top bulb sand
                    val topSand = Path().apply {
                        moveTo(cx - 30f, cy * 0.7f)
                        cubicTo(cx - 15f, cy * 0.85f, cx + 15f, cy * 0.85f, cx + 30f, cy * 0.7f)
                        lineTo(cx + 8f, cy)
                        lineTo(cx - 8f, cy)
                        close()
                    }
                    drawPath(
                        path = topSand,
                        brush = Brush.verticalGradient(
                            colors = listOf(AmberSecondary.copy(alpha = 0.85f), CoralPrimary)
                        ),
                        style = Fill
                    )

                    // Trickle stream
                    drawLine(
                        color = AmberSecondary,
                        start = Offset(cx, cy),
                        end = Offset(cx, h - capHeight - 20f),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round
                    )

                    // Bottom bulb sand mound
                    val bottomSand = Path().apply {
                        moveTo(cx - 38f, h - capHeight - 2f)
                        cubicTo(
                            cx - 20f, h - capHeight - 34f,
                            cx + 20f, h - capHeight - 34f,
                            cx + 38f, h - capHeight - 2f
                        )
                        close()
                    }
                    drawPath(
                        path = bottomSand,
                        brush = Brush.verticalGradient(
                            colors = listOf(CoralPrimary, AmberSecondary)
                        ),
                        style = Fill
                    )
                }
            }

            // Right Callouts
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(70.dp)
            ) {
                CalloutText(text = "Greater\nPurpose")
                CalloutText(text = "A Better\nYou")
            }
        }
    }
}

@Composable
private fun CalloutText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp
        ),
        color = Color(0xFFE2E8F0)
    )
}
