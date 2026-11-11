package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.entity.Complaint
import com.example.data.local.entity.Student
import com.example.data.local.entity.Teacher
import com.example.ui.theme.SchoolAmber
import com.example.ui.theme.SchoolBlueDark
import com.example.ui.theme.SchoolBluePrimary
import com.example.ui.theme.SchoolCrimson
import com.example.ui.theme.SchoolGold
import com.example.ui.theme.SchoolGreen
import com.example.ui.viewmodel.ComplaintTemplate
import com.example.util.CommunicationHelper

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NewComplaintDialog(
    students: List<Student>,
    teachers: List<Teacher>,
    templates: List<ComplaintTemplate>,
    preselectedStudent: Student? = null,
    onDismiss: () -> Unit,
    onSubmit: (
        student: Student,
        teacher: Teacher,
        category: String,
        severity: String,
        title: String,
        description: String,
        expectedAction: String,
        sendWhatsAppImmediately: Boolean
    ) -> Unit
) {
    val context = LocalContext.current

    var selectedStudent by remember {
        mutableStateOf(preselectedStudent ?: students.firstOrNull())
    }
    var selectedTeacher by remember {
        mutableStateOf(teachers.firstOrNull())
    }

    val categories = listOf(
        "Academic Performance",
        "Classroom Misbehavior",
        "Attendance & Punctuality",
        "Incomplete Homework",
        "Discipline & Bullying",
        "Uniform & Grooming"
    )

    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var selectedSeverity by remember { mutableStateOf("Moderate") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expectedAction by remember { mutableStateOf("") }

    var studentMenuExpanded by remember { mutableStateOf(false) }
    var teacherMenuExpanded by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp)
                .testTag("new_complaint_dialog"),
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
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Write Complaint to Parent",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SchoolBluePrimary
                            )
                        )
                        Text(
                            text = "The Neo Suffah Model School • Official Report",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_new_complaint_dialog")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Templates Bar
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FA)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = SchoolAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Quick Fill Templates for Teachers:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SchoolBlueDark
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            templates.forEach { t ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                    modifier = Modifier.clickable {
                                        title = t.title
                                        description = t.description
                                        expectedAction = t.expectedAction
                                        selectedCategory = t.category
                                        selectedSeverity = t.severity
                                        Toast.makeText(context, "Loaded: ${t.title}", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Text(
                                        text = t.title,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = SchoolBluePrimary,
                                            fontSize = 11.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Select Student Dropdown
                Text(
                    text = "1. Target Student",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))

                ExposedDropdownMenuBox(
                    expanded = studentMenuExpanded,
                    onExpandedChange = { studentMenuExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedStudent?.let { "${it.name} (${it.rollNumber} - ${it.grade})" } ?: "Select Student",
                        onValueChange = {},
                        readOnly = true,
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = SchoolBluePrimary)
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = studentMenuExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("student_select_dropdown")
                    )
                    ExposedDropdownMenu(
                        expanded = studentMenuExpanded,
                        onDismissRequest = { studentMenuExpanded = false }
                    ) {
                        students.forEach { s ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(text = "${s.name} (Roll: ${s.rollNumber})", fontWeight = FontWeight.SemiBold)
                                        Text(
                                            text = "Class: ${s.grade} • Father: ${s.fatherName} • 📞 ${s.parentPhone}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                },
                                onClick = {
                                    selectedStudent = s
                                    studentMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Select Teacher Dropdown
                Text(
                    text = "2. Reporting Teacher",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))

                ExposedDropdownMenuBox(
                    expanded = teacherMenuExpanded,
                    onExpandedChange = { teacherMenuExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedTeacher?.let { "${it.name} (${it.subject})" } ?: "Select Teacher",
                        onValueChange = {},
                        readOnly = true,
                        leadingIcon = {
                            Icon(Icons.Default.School, contentDescription = null, tint = SchoolBluePrimary)
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = teacherMenuExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("teacher_select_dropdown")
                    )
                    ExposedDropdownMenu(
                        expanded = teacherMenuExpanded,
                        onDismissRequest = { teacherMenuExpanded = false }
                    ) {
                        teachers.forEach { t ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(text = t.name, fontWeight = FontWeight.SemiBold)
                                        Text(
                                            text = "Subject: ${t.subject} • ${t.assignedGrade}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                },
                                onClick = {
                                    selectedTeacher = t
                                    teacherMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Category Chips
                Text(
                    text = "3. Issue Category",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SchoolBluePrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Severity Level
                Text(
                    text = "4. Priority / Severity Level",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val severities = listOf(
                        "Low" to Color(0xFF0284C7),
                        "Moderate" to SchoolAmber,
                        "Critical" to SchoolCrimson
                    )
                    severities.forEach { (sev, col) ->
                        val isSelected = selectedSeverity.equals(sev, ignoreCase = true)
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedSeverity = sev },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) col.copy(alpha = 0.15f) else Color(0xFFF8FAFC),
                            border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) col else Color(0xFFCBD5E1))
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = sev,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) col else Color(0xFF475569),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Title / Subject
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Complaint Title / Summary *") },
                    placeholder = { Text("e.g. Inattention in Mathematics, Failing Weekly Test") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("complaint_title_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Description (Teacher Statement)
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Detailed Teacher's Statement *") },
                    placeholder = { Text("Describe specific incidents, dates, test scores, or behavioral issues...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("complaint_description_input"),
                    minLines = 4,
                    maxLines = 7
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Expected Parent Action
                OutlinedTextField(
                    value = expectedAction,
                    onValueChange = { expectedAction = it },
                    label = { Text("Required Parent Action (Optional)") },
                    placeholder = { Text("e.g. Check child's diary daily; Visit school Saturday 10:00 AM") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("complaint_expected_action_input"),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Button(
                    onClick = {
                        val student = selectedStudent
                        val teacher = selectedTeacher
                        if (student == null) {
                            Toast.makeText(context, "Please select a student", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (teacher == null) {
                            Toast.makeText(context, "Please select a reporting teacher", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (title.isBlank()) {
                            Toast.makeText(context, "Please enter a complaint title", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (description.isBlank()) {
                            Toast.makeText(context, "Please provide the complaint details", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        onSubmit(
                            student,
                            teacher,
                            selectedCategory,
                            selectedSeverity,
                            title,
                            description,
                            expectedAction,
                            true // send WhatsApp immediately
                        )
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_and_send_whatsapp_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Save & Send via WhatsApp to Parent",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        val student = selectedStudent
                        val teacher = selectedTeacher
                        if (student == null) {
                            Toast.makeText(context, "Please select a student", Toast.LENGTH_SHORT).show()
                            return@OutlinedButton
                        }
                        if (teacher == null) {
                            Toast.makeText(context, "Please select a teacher", Toast.LENGTH_SHORT).show()
                            return@OutlinedButton
                        }
                        if (title.isBlank() || description.isBlank()) {
                            Toast.makeText(context, "Title and details are required", Toast.LENGTH_SHORT).show()
                            return@OutlinedButton
                        }

                        onSubmit(
                            student,
                            teacher,
                            selectedCategory,
                            selectedSeverity,
                            title,
                            description,
                            expectedAction,
                            false // save to record only
                        )
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("save_record_only_btn"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Save to School Records Only", color = SchoolBluePrimary)
                }
            }
        }
    }
}
