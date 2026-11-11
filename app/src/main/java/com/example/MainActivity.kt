package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.entity.Complaint
import com.example.data.local.entity.Student
import com.example.ui.screens.ComplaintDetailDialog
import com.example.ui.screens.ComplaintsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.NewComplaintDialog
import com.example.ui.screens.SchoolInfoScreen
import com.example.ui.screens.StudentDetailDialog
import com.example.ui.screens.StudentsScreen
import com.example.ui.screens.TeachersScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SchoolBluePrimary
import com.example.ui.viewmodel.SchoolViewModel
import com.example.util.CommunicationHelper

enum class Screen(val title: String, val icon: ImageVector) {
    Dashboard("Home", Icons.Default.Dashboard),
    Complaints("Complaints", Icons.Default.AssignmentLate),
    Students("Students", Icons.Default.Group),
    Teachers("Faculty", Icons.Default.School),
    SchoolInfo("School", Icons.Default.Info)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(
    viewModel: SchoolViewModel = viewModel()
) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf(Screen.Dashboard) }

    // Dialog state
    var showNewComplaintDialog by remember { mutableStateOf(false) }
    var preselectedStudentForComplaint by remember { mutableStateOf<Student?>(null) }
    var selectedComplaintForDetail by remember { mutableStateOf<Complaint?>(null) }
    var selectedStudentForDetail by remember { mutableStateOf<Student?>(null) }

    // Observe flows
    val allStudents by viewModel.allStudents.collectAsStateWithLifecycle()
    val allTeachers by viewModel.allTeachers.collectAsStateWithLifecycle()
    val allComplaints by viewModel.allComplaints.collectAsStateWithLifecycle()
    val filteredComplaints by viewModel.filteredComplaints.collectAsStateWithLifecycle()
    val filteredStudents by viewModel.filteredStudents.collectAsStateWithLifecycle()

    val studentCount by viewModel.studentCount.collectAsStateWithLifecycle()
    val pendingCount by viewModel.pendingComplaintsCount.collectAsStateWithLifecycle()
    val criticalCount by viewModel.criticalComplaintsCount.collectAsStateWithLifecycle()
    val resolvedCount by viewModel.resolvedComplaintsCount.collectAsStateWithLifecycle()

    val complaintFilter by viewModel.complaintFilter.collectAsStateWithLifecycle()
    val studentSearchQuery by viewModel.studentSearchQuery.collectAsStateWithLifecycle()
    val studentGradeFilter by viewModel.selectedGradeFilter.collectAsStateWithLifecycle()

    // Back handling: return to Dashboard if on any other tab
    if (currentScreen != Screen.Dashboard) {
        BackHandler {
            currentScreen = Screen.Dashboard
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                Screen.values().forEach { screen ->
                    val selected = currentScreen == screen
                    NavigationBarItem(
                        selected = selected,
                        onClick = { currentScreen = screen },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = {
                            Text(
                                text = screen.title,
                                fontSize = 11.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SchoolBluePrimary,
                            selectedTextColor = SchoolBluePrimary,
                            indicatorColor = Color(0xFFD6E2FB),
                            unselectedIconColor = Color(0xFF64748B),
                            unselectedTextColor = Color(0xFF64748B)
                        ),
                        modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        when (currentScreen) {
            Screen.Dashboard -> {
                DashboardScreen(
                    studentCount = studentCount,
                    pendingCount = pendingCount,
                    criticalCount = criticalCount,
                    resolvedCount = resolvedCount,
                    recentComplaints = allComplaints,
                    onNewComplaintClick = {
                        preselectedStudentForComplaint = null
                        showNewComplaintDialog = true
                    },
                    onAddStudentClick = {
                        currentScreen = Screen.Students
                    },
                    onViewAllComplaintsClick = {
                        currentScreen = Screen.Complaints
                    },
                    onComplaintClick = { complaint ->
                        selectedComplaintForDetail = complaint
                    },
                    onStatusClick = { complaint ->
                        selectedComplaintForDetail = complaint
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.Complaints -> {
                ComplaintsScreen(
                    complaints = filteredComplaints,
                    filter = complaintFilter,
                    onSearchChange = viewModel::updateComplaintSearchQuery,
                    onCategoryChange = viewModel::setComplaintCategoryFilter,
                    onStatusChange = viewModel::setComplaintStatusFilter,
                    onNewComplaintClick = {
                        preselectedStudentForComplaint = null
                        showNewComplaintDialog = true
                    },
                    onComplaintClick = { complaint ->
                        selectedComplaintForDetail = complaint
                    },
                    onStatusClick = { complaint ->
                        selectedComplaintForDetail = complaint
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.Students -> {
                StudentsScreen(
                    students = filteredStudents,
                    complaints = allComplaints,
                    searchQuery = studentSearchQuery,
                    gradeFilter = studentGradeFilter,
                    onSearchChange = viewModel::updateStudentSearchQuery,
                    onGradeFilterChange = viewModel::setStudentGradeFilter,
                    onStudentClick = { student ->
                        selectedStudentForDetail = student
                    },
                    onNewComplaintForStudent = { student ->
                        preselectedStudentForComplaint = student
                        showNewComplaintDialog = true
                    },
                    onAddStudent = { n, r, g, f, p, sp, gn, a ->
                        viewModel.addStudent(n, r, g, f, p, sp, gn, a) {
                            Toast.makeText(context, "Student $n registered", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.Teachers -> {
                TeachersScreen(
                    teachers = allTeachers,
                    complaints = allComplaints,
                    onAddTeacher = { n, s, g, p, c ->
                        viewModel.addTeacher(n, s, g, p, c) {
                            Toast.makeText(context, "Teacher $n added", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.SchoolInfo -> {
                SchoolInfoScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }

    // New Complaint Dialog
    if (showNewComplaintDialog) {
        NewComplaintDialog(
            students = allStudents,
            teachers = allTeachers,
            templates = viewModel.complaintTemplates,
            preselectedStudent = preselectedStudentForComplaint,
            onDismiss = {
                showNewComplaintDialog = false
                preselectedStudentForComplaint = null
            },
            onSubmit = { student, teacher, category, severity, title, desc, action, sendWhatsApp ->
                viewModel.saveComplaint(student, teacher, category, severity, title, desc, action) { id ->
                    Toast.makeText(context, "Complaint saved for ${student.name}", Toast.LENGTH_SHORT).show()
                    if (sendWhatsApp) {
                        val tempComplaint = Complaint(
                            id = id,
                            studentId = student.id,
                            studentName = student.name,
                            studentRoll = student.rollNumber,
                            grade = student.grade,
                            parentPhone = student.parentPhone,
                            teacherId = teacher.id,
                            teacherName = teacher.name,
                            subject = teacher.subject,
                            category = category,
                            severity = severity,
                            title = title,
                            description = desc,
                            expectedAction = action,
                            incidentDate = "Today"
                        )
                        CommunicationHelper.openWhatsAppNotice(context, tempComplaint)
                    }
                }
            }
        )
    }

    // Complaint Detail & Status Resolution Dialog
    selectedComplaintForDetail?.let { complaint ->
        ComplaintDetailDialog(
            complaint = complaint,
            onDismiss = { selectedComplaintForDetail = null },
            onUpdateStatus = { newStatus, feedback, isResolved ->
                viewModel.updateComplaintStatus(complaint, newStatus, feedback, isResolved)
                Toast.makeText(context, "Status updated to: $newStatus", Toast.LENGTH_SHORT).show()
            },
            onDelete = {
                viewModel.deleteComplaint(complaint)
                Toast.makeText(context, "Complaint removed", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Student Detail Dialog
    selectedStudentForDetail?.let { student ->
        StudentDetailDialog(
            student = student,
            complaints = allComplaints,
            onDismiss = { selectedStudentForDetail = null },
            onNewComplaintForStudent = { s ->
                preselectedStudentForComplaint = s
                showNewComplaintDialog = true
            },
            onComplaintClick = { c ->
                selectedComplaintForDetail = c
            }
        )
    }
}
