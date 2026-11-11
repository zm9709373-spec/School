package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entity.Complaint
import com.example.data.local.entity.Student
import com.example.data.local.entity.Teacher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SchoolRepository(private val database: AppDatabase) {
    private val studentDao = database.studentDao()
    private val teacherDao = database.teacherDao()
    private val complaintDao = database.complaintDao()

    val allStudents: Flow<List<Student>> = studentDao.getAllStudents()
    val allTeachers: Flow<List<Teacher>> = teacherDao.getAllTeachers()
    val allComplaints: Flow<List<Complaint>> = complaintDao.getAllComplaints()
    val activeCriticalComplaints: Flow<List<Complaint>> = complaintDao.getActiveCriticalComplaints()
    val pendingComplaints: Flow<List<Complaint>> = complaintDao.getPendingComplaints()

    val studentCount: Flow<Int> = studentDao.getStudentCount()
    val teacherCount: Flow<Int> = teacherDao.getTeacherCount()
    val totalComplaintsCount: Flow<Int> = complaintDao.getTotalComplaintsCount()
    val pendingComplaintsCount: Flow<Int> = complaintDao.getPendingComplaintsCount()
    val resolvedComplaintsCount: Flow<Int> = complaintDao.getResolvedComplaintsCount()
    val criticalComplaintsCount: Flow<Int> = complaintDao.getCriticalComplaintsCount()

    fun getComplaintsForStudent(studentId: Long): Flow<List<Complaint>> =
        complaintDao.getComplaintsForStudent(studentId)

    suspend fun getStudentById(id: Long): Student? = studentDao.getStudentById(id)
    suspend fun getComplaintById(id: Long): Complaint? = complaintDao.getComplaintById(id)
    suspend fun getTeacherById(id: Long): Teacher? = teacherDao.getTeacherById(id)

    suspend fun insertStudent(student: Student): Long = studentDao.insertStudent(student)
    suspend fun updateStudent(student: Student) = studentDao.updateStudent(student)
    suspend fun deleteStudent(student: Student) = studentDao.deleteStudent(student)

    suspend fun insertTeacher(teacher: Teacher): Long = teacherDao.insertTeacher(teacher)
    suspend fun updateTeacher(teacher: Teacher) = teacherDao.updateTeacher(teacher)
    suspend fun deleteTeacher(teacher: Teacher) = teacherDao.deleteTeacher(teacher)

    suspend fun insertComplaint(complaint: Complaint): Long = complaintDao.insertComplaint(complaint)
    suspend fun updateComplaint(complaint: Complaint) = complaintDao.updateComplaint(complaint)
    suspend fun deleteComplaint(complaint: Complaint) = complaintDao.deleteComplaint(complaint)

    suspend fun seedInitialDataIfEmpty() {
        val currentCount = studentDao.getStudentCount().first()
        if (currentCount == 0) {
            // Seed teachers
            val teachers = listOf(
                Teacher(
                    name = "Sir Muhammad Tariq",
                    subject = "Mathematics",
                    assignedGrade = "Grade 9 & 10",
                    phone = "+92 3155261941",
                    employeeCode = "TCH-M01"
                ),
                Teacher(
                    name = "Sir Shahab Ahmad",
                    subject = "Physics & Chemistry",
                    assignedGrade = "Grade 9 & 10",
                    phone = "+92 3155261941",
                    employeeCode = "TCH-S02"
                ),
                Teacher(
                    name = "Madam Farzana Bibi",
                    subject = "English Literature",
                    assignedGrade = "Grade 8 & 9",
                    phone = "+92 3155261941",
                    employeeCode = "TCH-E03"
                ),
                Teacher(
                    name = "Sir Usman Ali",
                    subject = "Urdu & Islamiyat",
                    assignedGrade = "Grade 7 & 8",
                    phone = "+92 3155261941",
                    employeeCode = "TCH-U04"
                ),
                Teacher(
                    name = "Sir Bilal Khan",
                    subject = "Computer Science",
                    assignedGrade = "Grade 6 to 10",
                    phone = "+92 3155261941",
                    employeeCode = "TCH-C05"
                )
            )
            teacherDao.insertTeachers(teachers)
            val insertedTeachers = teacherDao.getAllTeachers().first()

            // Seed students
            val students = listOf(
                Student(
                    rollNumber = "NS-101",
                    name = "Hamza Tariq",
                    grade = "Grade 9-Science",
                    fatherName = "Tariq Mehmood",
                    parentPhone = "+923155261941",
                    secondaryPhone = "+923001234567",
                    gender = "Male",
                    address = "Garhi Daulat Zai, Garhi Kapoora"
                ),
                Student(
                    rollNumber = "NS-102",
                    name = "Bilal Ahmad",
                    grade = "Grade 9-Science",
                    fatherName = "Ahmad Jan",
                    parentPhone = "+923019876543",
                    secondaryPhone = "",
                    gender = "Male",
                    address = "Tango Adda, Garhi Kapoora"
                ),
                Student(
                    rollNumber = "NS-201",
                    name = "Ayesha Bibi",
                    grade = "Grade 10-Science",
                    fatherName = "Fazal Rabbi",
                    parentPhone = "+923334567890",
                    secondaryPhone = "",
                    gender = "Female",
                    address = "Main Bazar Garhi Kapoora, Mardan"
                ),
                Student(
                    rollNumber = "NS-202",
                    name = "Daniyal Khan",
                    grade = "Grade 8-A",
                    fatherName = "Khan Bahadar",
                    parentPhone = "+923451234567",
                    secondaryPhone = "",
                    gender = "Male",
                    address = "Garhi Daulat Zai, Mardan"
                ),
                Student(
                    rollNumber = "NS-305",
                    name = "Zubair Shah",
                    grade = "Grade 7-B",
                    fatherName = "Syed Noor Shah",
                    parentPhone = "+923155261941",
                    secondaryPhone = "",
                    gender = "Male",
                    address = "Near Tango Adda, Garhi Kapoora"
                ),
                Student(
                    rollNumber = "NS-306",
                    name = "Usman Farooq",
                    grade = "Grade 9-Science",
                    fatherName = "Farooq Azam",
                    parentPhone = "+923005551234",
                    secondaryPhone = "",
                    gender = "Male",
                    address = "Garhi Kapoora, Mardan"
                ),
                Student(
                    rollNumber = "NS-401",
                    name = "Fatima Zahra",
                    grade = "Grade 8-A",
                    fatherName = "Muhammad Zahir",
                    parentPhone = "+923214567890",
                    secondaryPhone = "",
                    gender = "Female",
                    address = "Daulat Zai Road, Mardan"
                )
            )
            studentDao.insertStudents(students)
            val insertedStudents = studentDao.getAllStudents().first()

            val teacherTariq = insertedTeachers.firstOrNull { it.name.contains("Tariq") } ?: insertedTeachers[0]
            val teacherShahab = insertedTeachers.firstOrNull { it.name.contains("Shahab") } ?: insertedTeachers[0]
            val teacherUsman = insertedTeachers.firstOrNull { it.name.contains("Usman") } ?: insertedTeachers[0]

            val hamza = insertedStudents.firstOrNull { it.rollNumber == "NS-101" } ?: insertedStudents[0]
            val daniyal = insertedStudents.firstOrNull { it.rollNumber == "NS-202" } ?: insertedStudents[0]
            val zubair = insertedStudents.firstOrNull { it.rollNumber == "NS-305" } ?: insertedStudents[0]
            val bilal = insertedStudents.firstOrNull { it.rollNumber == "NS-102" } ?: insertedStudents[0]

            val initialComplaints = listOf(
                Complaint(
                    studentId = hamza.id,
                    studentName = hamza.name,
                    studentRoll = hamza.rollNumber,
                    grade = hamza.grade,
                    parentPhone = hamza.parentPhone,
                    teacherId = teacherTariq.id,
                    teacherName = teacherTariq.name,
                    subject = teacherTariq.subject,
                    category = "Academic Performance",
                    severity = "Moderate",
                    title = "Consistent Low Score in Weekly Algebra Test",
                    description = "Hamza scored 11 out of 50 in the recent Algebra test. He struggles with algebraic factorization and seems disengaged during problem-solving sessions. Extra practice and home supervision are strongly recommended.",
                    expectedAction = "Check child's daily classwork copy and supervise 1 hour math practice at home.",
                    status = "Pending / Sent",
                    incidentDate = "09 Nov 2026",
                    createdAt = System.currentTimeMillis() - 86400000L * 2
                ),
                Complaint(
                    studentId = daniyal.id,
                    studentName = daniyal.name,
                    studentRoll = daniyal.rollNumber,
                    grade = daniyal.grade,
                    parentPhone = daniyal.parentPhone,
                    teacherId = teacherShahab.id,
                    teacherName = teacherShahab.name,
                    subject = teacherShahab.subject,
                    category = "Classroom Misbehavior",
                    severity = "Critical",
                    title = "Disrupting Science Lab & Insubordination",
                    description = "Daniyal engaged in rough play inside the science laboratory with glassware, ignoring repeated safety instructions from the teacher. When warned, he showed disrespectful attitude.",
                    expectedAction = "Father is requested to attend an urgent meeting with Principal & Teacher on Saturday at 10:00 AM.",
                    status = "Pending / Sent",
                    incidentDate = "10 Nov 2026",
                    createdAt = System.currentTimeMillis() - 86400000L * 1
                ),
                Complaint(
                    studentId = zubair.id,
                    studentName = zubair.name,
                    studentRoll = zubair.rollNumber,
                    grade = zubair.grade,
                    parentPhone = zubair.parentPhone,
                    teacherId = teacherUsman.id,
                    teacherName = teacherUsman.name,
                    subject = teacherUsman.subject,
                    category = "Attendance & Punctuality",
                    severity = "Moderate",
                    title = "Unexcused Absence for 3 Days This Week",
                    description = "Zubair has been absent on Monday, Tuesday, and Thursday without any leave application or prior notice from parents.",
                    expectedAction = "Submit written leave application and ensure regular attendance to avoid syllabus backlog.",
                    status = "Parent Acknowledged",
                    parentFeedback = "Father called and confirmed child had viral fever. Will send doctor slip.",
                    incidentDate = "08 Nov 2026",
                    createdAt = System.currentTimeMillis() - 86400000L * 3
                ),
                Complaint(
                    studentId = bilal.id,
                    studentName = bilal.name,
                    studentRoll = bilal.rollNumber,
                    grade = bilal.grade,
                    parentPhone = bilal.parentPhone,
                    teacherId = teacherTariq.id,
                    teacherName = teacherTariq.name,
                    subject = teacherTariq.subject,
                    category = "Incomplete Homework",
                    severity = "Low",
                    title = "Math Exercise 4.2 Not Completed",
                    description = "Bilal did not bring his completed homework notebook for the 2nd time this week. He claims he forgot it at home.",
                    expectedAction = "Please sign student school diary today after verifying homework completion.",
                    status = "Resolved",
                    parentFeedback = "Parent checked and signed diary; homework completed.",
                    incidentDate = "05 Nov 2026",
                    createdAt = System.currentTimeMillis() - 86400000L * 5,
                    isResolved = true
                )
            )
            complaintDao.insertComplaints(initialComplaints)
        }
    }
}
