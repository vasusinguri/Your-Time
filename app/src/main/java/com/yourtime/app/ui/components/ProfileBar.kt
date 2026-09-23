package com.yourtime.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourtime.app.domain.UserProfile
import com.yourtime.app.ui.theme.CyberCyan
import com.yourtime.app.ui.theme.EmeraldGreen
import com.yourtime.app.ui.theme.OledCard
import com.yourtime.app.ui.theme.OledCardBorder
import com.yourtime.app.ui.theme.OledSurface
import com.yourtime.app.ui.theme.TextMuted
import com.yourtime.app.ui.theme.TextWhite

@Composable
fun ProfileBar(
    profiles: List<UserProfile>,
    activeProfileId: String,
    onSelectProfile: (String) -> Unit,
    onAddProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        profiles.forEach { profile ->
            val isActive = profile.id == activeProfileId
            ProfileChip(
                profile = profile,
                isActive = isActive,
                onClick = { onSelectProfile(profile.id) }
            )
        }

        // Add Profile Button
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = OledSurface,
            border = BorderStroke(1.dp, OledCardBorder),
            modifier = Modifier.clickable(onClick = onAddProfile)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Profile",
                    tint = CyberCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Add",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    ),
                    color = CyberCyan
                )
            }
        }
    }
}

@Composable
private fun ProfileChip(
    profile: UserProfile,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isActive) CyberCyan else OledCardBorder
    val bgColor = if (isActive) OledCard else OledSurface
    val textColor = if (isActive) TextWhite else TextMuted
    val iconColor = if (isActive) CyberCyan else TextMuted

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = BorderStroke(if (isActive) 1.5.dp else 1.dp, borderColor),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = profile.name,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 13.sp
                ),
                color = textColor
            )

            if (profile.tag.isNotBlank() && profile.tag != profile.name) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "(${profile.tag})",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = if (isActive) EmeraldGreen else TextMuted.copy(alpha = 0.7f)
                )
            }
        }
    }
}
