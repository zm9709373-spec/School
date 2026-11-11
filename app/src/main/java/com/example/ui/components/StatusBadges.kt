package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SchoolAmber
import com.example.ui.theme.SchoolBluePrimary
import com.example.ui.theme.SchoolCrimson
import com.example.ui.theme.SchoolGreen

@Composable
fun SeverityBadge(severity: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (severity.lowercase()) {
        "critical" -> Color(0xFFFFEBEE) to SchoolCrimson
        "moderate" -> Color(0xFFFEF3C7) to Color(0xFFB45309)
        else -> Color(0xFFE0F2FE) to SchoolBluePrimary
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = severity,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StatusBadge(status: String, isResolved: Boolean, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = if (isResolved || status.contains("Resolved", ignoreCase = true)) {
        Color(0xFFDCFCE7) to SchoolGreen
    } else when {
        status.contains("Meeting", ignoreCase = true) -> Color(0xFFFFEDD5) to Color(0xFFC2410C)
        status.contains("Acknowledged", ignoreCase = true) -> Color(0xFFE0E7FF) to Color(0xFF4338CA)
        else -> Color(0xFFF1F5F9) to Color(0xFF475569)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun CategoryChip(category: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = category,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
