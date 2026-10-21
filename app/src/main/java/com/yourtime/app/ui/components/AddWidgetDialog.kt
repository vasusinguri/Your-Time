package com.yourtime.app.ui.components

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourtime.app.ui.theme.CyberCyan
import com.yourtime.app.ui.theme.CyberGradient
import com.yourtime.app.ui.theme.EmeraldGreen
import com.yourtime.app.ui.theme.OledCard
import com.yourtime.app.ui.theme.OledCardBorder
import com.yourtime.app.ui.theme.OledSurface
import com.yourtime.app.ui.theme.TextMuted
import com.yourtime.app.ui.theme.TextWhite
import com.yourtime.app.widget.AgeWidgetProvider

@Composable
fun AddWidgetDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val isPinSupported = appWidgetManager != null && appWidgetManager.isRequestPinAppWidgetSupported

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Widgets,
                    contentDescription = null,
                    tint = CyberCyan,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Home Screen Widgets",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextWhite
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Track your time at a glance without even opening the app.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )

                // Widget 1: Age & Birthday
                WidgetOptionCard(
                    title = "Age & Birthday Widget",
                    subtitle = "Shows live Years, Months, Days & Next Birthday countdown in deep OLED styling.",
                    icon = Icons.Outlined.Cake,
                    accentColor = CyberCyan,
                    isPinSupported = isPinSupported,
                    onPinClicked = {
                        pinWidget(context, appWidgetManager, AgeWidgetProvider::class.java)
                    }
                )

                // Manual hint
                Text(
                    text = "Tip: You can also touch and hold any empty area on your phone's home screen, tap 'Widgets', and select 'Your Time'.",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextMuted.copy(alpha = 0.8f)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = CyberCyan, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = OledCard
    )
}

@Composable
private fun WidgetOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    isPinSupported: Boolean,
    onPinClicked: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = OledSurface),
        border = BorderStroke(1.dp, OledCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    ),
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (isPinSupported) {
                Button(
                    onClick = onPinClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .background(CyberGradient, shape = RoundedCornerShape(10.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Pin to Home Screen",
                        color = TextWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun pinWidget(context: Context, manager: AppWidgetManager?, providerClass: Class<*>) {
    if (manager == null) return
    val provider = ComponentName(context, providerClass)
    if (manager.isRequestPinAppWidgetSupported) {
        manager.requestPinAppWidget(provider, null, null)
    } else {
        Toast.makeText(
            context,
            "Pinning is not supported by your launcher. Please add from the home screen widget picker.",
            Toast.LENGTH_LONG
        ).show()
    }
}
