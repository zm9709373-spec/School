package com.example.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.entity.Complaint
import com.example.ui.components.CategoryChip
import com.example.ui.components.SeverityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CardBorder
import com.example.ui.theme.SchoolBlueDark
import com.example.ui.theme.SchoolBluePrimary
import com.example.ui.theme.SchoolCrimson
import com.example.ui.theme.SchoolGreen
import com.example.util.CommunicationHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintDetailDialog(
    complaint: Complaint,
    onDismiss: () -> Unit,
    onUpdateStatus: (newStatus: String, feedback: String, isResolved: Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    var selectedStatus by remember { mutableStateOf(complaint.status) }
    var parentFeedback by remember { mutableStateOf(complaint.parentFeedback) }
    var isResolved by remember { mutableStateOf(complaint.isResolved) }
    var isStatusExpanded by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val statusOptions = listOf(
        "Pending / Sent",
        "Parent Acknowledged",
        "Meeting Scheduled",
        "Resolved / Improved"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 24.dp)
                .testTag("complaint_detail_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top row with title & close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Student Conduct & Performance Report",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SchoolBluePrimary
                            )
                        )
                        Text(
                            text = "The Neo Suffah Model School",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_detail_dialog")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Student & Teacher Information Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = complaint.studentName,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            SeverityBadge(severity = complaint.severity)
                        }
                        Text(
                            text = "Roll: ${complaint.studentRoll} • Grade: ${complaint.grade}",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF475569))
                        )
                        Text(
                            text = "Parent Phone: ${complaint.parentPhone}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = SchoolBluePrimary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Reporting Teacher: ${complaint.teacherName} (${complaint.subject})",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155))
                        )
                        Text(
                            text = "Date: ${complaint.incidentDate}",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Category & Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryChip(category = complaint.category)
                    StatusBadge(status = complaint.status, isResolved = complaint.isResolved)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Subject / Issue Title
                Text(
                    text = complaint.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Teacher Statement
                Text(
                    text = "Teacher's Report:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = complaint.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF1E293B),
                        lineHeight = 20.sp
                    )
                )

                if (complaint.expectedAction.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Required Action by Parent:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = SchoolCrimson
                        )
                    )
                    Text(
                        text = complaint.expectedAction,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF334155),
                            lineHeight = 18.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Direct Parent Dispatch Section
                Text(
                    text = "Dispatch Notice to Parent",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SchoolBluePrimary
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { CommunicationHelper.openWhatsAppNotice(context, complaint) },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(44.dp)
                            .testTag("dialog_send_whatsapp"),
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Send WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { CommunicationHelper.dialParent(context, complaint.parentPhone) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("dialog_call_parent"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Call", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { CommunicationHelper.sendSmsNotice(context, complaint) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("dialog_send_sms"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Message, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "SMS", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Status & Feedback Update Form
                Text(
                    text = "Update Status & Follow-up Notes",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = isStatusExpanded,
                    onExpandedChange = { isStatusExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedStatus,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Complaint Status") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isStatusExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("status_dropdown")
                    )
                    ExposedDropdownMenu(
                        expanded = isStatusExpanded,
                        onDismissRequest = { isStatusExpanded = false }
                    ) {
                        statusOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedStatus = option
                                    if (option.contains("Resolved", ignoreCase = true)) {
                                        isResolved = true
                                    }
                                    isStatusExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = parentFeedback,
                    onValueChange = { parentFeedback = it },
                    label = { Text("Parent Response / Follow-up Remarks") },
                    placeholder = { Text("e.g. Father visited school on Saturday, pledged daily diary inspection") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("parent_feedback_input"),
                    minLines = 2,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isResolved,
                        onCheckedChange = {
                            isResolved = it
                            if (it) selectedStatus = "Resolved / Improved"
                        },
                        modifier = Modifier.testTag("resolve_checkbox")
                    )
                    Text(
                        text = "Issue has been resolved / student behavior improved",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Save Status & Delete Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { showDeleteConfirm = true },
                        colors = ButtonDefaults.textButtonColors(contentColor = SchoolCrimson),
                        modifier = Modifier.testTag("delete_complaint_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            onUpdateStatus(selectedStatus, parentFeedback, isResolved)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolBluePrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("save_status_btn")
                    ) {
                        Text("Save Status Updates", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Complaint?") },
            text = { Text("Are you sure you want to permanently delete this complaint record?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDelete()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolCrimson)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
