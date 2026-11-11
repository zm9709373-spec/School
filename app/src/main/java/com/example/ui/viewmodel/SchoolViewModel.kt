package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.Complaint
import com.example.data.local.entity.Student
import com.example.data.local.entity.Teacher
import com.example.data.repository.SchoolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ComplaintFilter(
    val searchQuery: String = "",
    val category: String = "All",
    val status: String = "All",
    val severity: String = "All",
    val grade: String = "All"
)

data class ComplaintTemplate(
    val title: String,
    val category: String,
    val severity: String,
    val description: String,
    val expectedAction: String
)

class SchoolViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val repository = SchoolRepository(database)

    val allStudents: StateFlow<List<Student>> = repository.allStudents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTeachers: StateFlow<List<Teacher>> = repository.allTeachers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allComplaints: StateFlow<List<Complaint>> = repository.allComplaints
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studentCount: StateFlow<Int> = repository.studentCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val teacherCount: StateFlow<Int> = repository.teacherCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalComplaintsCount: StateFlow<Int> = repository.totalComplaintsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendingComplaintsCount: StateFlow<Int> = repository.pendingComplaintsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val resolvedComplaintsCount: StateFlow<Int> = repository.resolvedComplaintsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val criticalComplaintsCount: StateFlow<Int> = repository.criticalComplaintsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Filtering State
    private val _complaintFilter = MutableStateFlow(ComplaintFilter())
    val complaintFilter: StateFlow<ComplaintFilter> = _complaintFilter.asStateFlow()

    // Student Search State
    private val _studentSearchQuery = MutableStateFlow("")
    val studentSearchQuery: StateFlow<String> = _studentSearchQuery.asStateFlow()

    private val _selectedGradeFilter = MutableStateFlow("All")
    val selectedGradeFilter: StateFlow<String> = _selectedGradeFilter.asStateFlow()

    // Filtered Complaints Flow
    val filteredComplaints: StateFlow<List<Complaint>> = combine(
        allComplaints,
        _complaintFilter
    ) { list, filter ->
        list.filter { c ->
            val matchesQuery = filter.searchQuery.isBlank() ||
                c.studentName.contains(filter.searchQuery, ignoreCase = true) ||
                c.studentRoll.contains(filter.searchQuery, ignoreCase = true) ||
                c.title.contains(filter.searchQuery, ignoreCase = true) ||
                c.teacherName.contains(filter.searchQuery, ignoreCase = true)

            val matchesCategory = filter.category == "All" || c.category.equals(filter.category, ignoreCase = true)
            val matchesStatus = when (filter.status) {
                "All" -> true
                "Pending" -> !c.isResolved
                "Resolved" -> c.isResolved
                else -> c.status.equals(filter.status, ignoreCase = true)
            }
            val matchesSeverity = filter.severity == "All" || c.severity.equals(filter.severity, ignoreCase = true)
            val matchesGrade = filter.grade == "All" || c.grade.equals(filter.grade, ignoreCase = true)

            matchesQuery && matchesCategory && matchesStatus && matchesSeverity && matchesGrade
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Students Flow
    val filteredStudents: StateFlow<List<Student>> = combine(
        allStudents,
        _studentSearchQuery,
        _selectedGradeFilter
    ) { students, query, grade ->
        students.filter { s ->
            val matchesQuery = query.isBlank() ||
                s.name.contains(query, ignoreCase = true) ||
                s.rollNumber.contains(query, ignoreCase = true) ||
                s.fatherName.contains(query, ignoreCase = true)
            val matchesGrade = grade == "All" || s.grade.equals(grade, ignoreCase = true)
            matchesQuery && matchesGrade
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Ready-made quick templates for teachers to rapidly file common issues
    val complaintTemplates = listOf(
        ComplaintTemplate(
            title = "Frequent Low Marks in Weekly Test",
            category = "Academic Performance",
            severity = "Moderate",
            description = "Student consistently scored below 35% in recent weekly subject assessments. Does not demonstrate understanding of core topics taught in class.",
            expectedAction = "Parent is requested to oversee 1.5 hours of daily evening study and review test copy."
        ),
        ComplaintTemplate(
            title = "Class Disruption & Talking in Lecture",
            category = "Classroom Misbehavior",
            severity = "Moderate",
            description = "Student repeatedly talks out of turn, interrupts the teacher, and distracts classmates during instruction despite verbal warnings.",
            expectedAction = "Please counsel your child at home regarding classroom etiquette and respect for educators."
        ),
        ComplaintTemplate(
            title = "Unexcused Absence & Truancy",
            category = "Attendance & Punctuality",
            severity = "Moderate",
            description = "Student has remained absent without submitting an authorized medical certificate or signed leave application.",
            expectedAction = "Send signed leave explanation immediately. Consistent attendance is mandatory for board exam eligibility."
        ),
        ComplaintTemplate(
            title = "Repeated Incomplete Homework",
            category = "Incomplete Homework",
            severity = "Low",
            description = "Student failed to submit assigned homework on three consecutive occasions, leaving notebooks blank or incomplete.",
            expectedAction = "Kindly sign child's school diary every evening after verifying task completion."
        ),
        ComplaintTemplate(
            title = "Disrespect & Physical Altercation",
            category = "Discipline & Bullying",
            severity = "Critical",
            description = "Student engaged in aggressive verbal disrespect and an altercation with fellow students during recess. Violates school code of conduct.",
            expectedAction = "Urgent: Parent must visit the Principal's office within 24 hours to address this disciplinary violation."
        ),
        ComplaintTemplate(
            title = "Improper School Uniform & Late Arrival",
            category = "Uniform & Grooming",
            severity = "Low",
            description = "Student arrived 25 minutes after school morning assembly without proper school necktie/black shoes.",
            expectedAction = "Ensure student adheres to the official school uniform policy and arrives by 7:45 AM daily."
        )
    )

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun updateComplaintSearchQuery(query: String) {
        _complaintFilter.value = _complaintFilter.value.copy(searchQuery = query)
    }

    fun setComplaintCategoryFilter(category: String) {
        _complaintFilter.value = _complaintFilter.value.copy(category = category)
    }

    fun setComplaintStatusFilter(status: String) {
        _complaintFilter.value = _complaintFilter.value.copy(status = status)
    }

    fun setComplaintSeverityFilter(severity: String) {
        _complaintFilter.value = _complaintFilter.value.copy(severity = severity)
    }

    fun setComplaintGradeFilter(grade: String) {
        _complaintFilter.value = _complaintFilter.value.copy(grade = grade)
    }

    fun updateStudentSearchQuery(query: String) {
        _studentSearchQuery.value = query
    }

    fun setStudentGradeFilter(grade: String) {
        _selectedGradeFilter.value = grade
    }

    fun saveComplaint(
        student: Student,
        teacher: Teacher,
        category: String,
        severity: String,
        title: String,
        description: String,
        expectedAction: String,
        onComplete: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            val complaint = Complaint(
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
                title = title.trim(),
                description = description.trim(),
                expectedAction = expectedAction.trim(),
                status = "Pending / Sent",
                incidentDate = dateStr,
                createdAt = System.currentTimeMillis(),
                isResolved = false
            )
            val newId = repository.insertComplaint(complaint)
            onComplete(newId)
        }
    }

    fun updateComplaintStatus(
        complaint: Complaint,
        newStatus: String,
        parentFeedback: String,
        isResolved: Boolean
    ) {
        viewModelScope.launch {
            val updated = complaint.copy(
                status = newStatus,
                parentFeedback = parentFeedback.trim(),
                isResolved = isResolved,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateComplaint(updated)
        }
    }

    fun deleteComplaint(complaint: Complaint) {
        viewModelScope.launch {
            repository.deleteComplaint(complaint)
        }
    }

    fun addStudent(
        name: String,
        rollNumber: String,
        grade: String,
        fatherName: String,
        parentPhone: String,
        secondaryPhone: String,
        gender: String,
        address: String,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val student = Student(
                name = name.trim(),
                rollNumber = rollNumber.trim().uppercase(),
                grade = grade.trim(),
                fatherName = fatherName.trim(),
                parentPhone = parentPhone.trim(),
                secondaryPhone = secondaryPhone.trim(),
                gender = gender,
                address = address.trim().ifBlank { "Garhi Kapoora, Mardan" }
            )
            repository.insertStudent(student)
            onComplete()
        }
    }

    fun updateStudent(student: Student, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.updateStudent(student)
            onComplete()
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            repository.deleteStudent(student)
        }
    }

    fun addTeacher(
        name: String,
        subject: String,
        assignedGrade: String,
        phone: String,
        employeeCode: String,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val teacher = Teacher(
                name = name.trim(),
                subject = subject.trim(),
                assignedGrade = assignedGrade.trim(),
                phone = phone.trim().ifBlank { "+92 3155261941" },
                employeeCode = employeeCode.trim()
            )
            repository.insertTeacher(teacher)
            onComplete()
        }
    }
}
