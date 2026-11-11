package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.Complaint
import com.example.ui.components.ComplaintCard
import com.example.ui.components.SchoolHeader
import com.example.ui.theme.SchoolAmber
import com.example.ui.theme.SchoolBluePrimary
import com.example.ui.theme.SchoolCrimson
import com.example.ui.theme.SchoolGreen

@Composable
fun DashboardScreen(
    studentCount: Int,
    pendingCount: Int,
    criticalCount: Int,
    resolvedCount: Int,
    recentComplaints: List<Complaint>,
    onNewComplaintClick: () -> Unit,
    onAddStudentClick: () -> Unit,
    onViewAllComplaintsClick: () -> Unit,
    onComplaintClick: (Complaint) -> Unit,
    onStatusClick: (Complaint) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // School Header with Logo, Name & Address
        item {
            SchoolHeader()
        }

        // Summary Metric Cards (2x2 Grid)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "School Overview & Notice Hub",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Total Students",
                        value = "$studentCount",
                        icon = Icons.Default.Group,
                        accentColor = SchoolBluePrimary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Active Notices",
                        value = "$pendingCount",
                        icon = Icons.Default.AssignmentLate,
                        accentColor = SchoolAmber,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Critical Alerts",
                        value = "$criticalCount",
                        icon = Icons.Default.Warning,
                        accentColor = SchoolCrimson,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Resolved Cases",
                        value = "$resolvedCount",
                        icon = Icons.Default.CheckCircle,
                        accentColor = SchoolGreen,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Primary Action Button for Teacher
                Button(
                    onClick = onNewComplaintClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("dashboard_new_complaint_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolBluePrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Write New Complaint to Parent",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onAddStudentClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("dashboard_add_student_btn"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Add Student to Register", color = SchoolBluePrimary)
                }
            }
        }

        // Critical Attention Alert (if any)
        if (criticalCount > 0) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = SchoolCrimson,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Urgent Discipline Notices",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SchoolCrimson
                                )
                            )
                            Text(
                                text = "$criticalCount student(s) require urgent parent meeting.",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF7F1D1D))
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Section Title: Recent Complaints
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Complaints & Reports",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                )
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = SchoolBluePrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable { onViewAllComplaintsClick() }
                        .padding(4.dp)
                        .testTag("dashboard_view_all_complaints")
                )
            }
        }

        // List of recent complaints
        if (recentComplaints.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No complaints logged yet. Tap '+ Write New Complaint' to report an issue.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B))
                    )
                }
            }
        } else {
            items(recentComplaints.take(5), key = { it.id }) { complaint ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    ComplaintCard(
                        complaint = complaint,
                        onCardClick = { onComplaintClick(complaint) },
                        onStatusClick = { onStatusClick(complaint) }
                    )
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(accentColor.copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
